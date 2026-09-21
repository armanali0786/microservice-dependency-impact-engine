package com.dependencyimpact.graphservice.traversal;

import com.dependencyimpact.common.exceptions.ResourceNotFoundException;
import com.dependencyimpact.common.model.DependencyCriticality;
import com.dependencyimpact.common.model.DependencyType;
import com.dependencyimpact.common.model.FailureBehavior;
import com.dependencyimpact.common.model.NodeType;
import com.dependencyimpact.graphservice.cache.GraphCacheKeyBuilder;
import com.dependencyimpact.graphservice.dto.GraphEdgeDto;
import com.dependencyimpact.graphservice.dto.GraphNodeDto;
import com.dependencyimpact.graphservice.dto.GraphResponse;
import com.dependencyimpact.graphservice.entity.Dependency;
import com.dependencyimpact.graphservice.entity.ServiceLookup;
import com.dependencyimpact.graphservice.exception.GraphTraversalLimitExceededException;
import com.dependencyimpact.graphservice.repository.DependencyRepository;
import com.dependencyimpact.graphservice.repository.ServiceLookupRepository;
import com.dependencyimpact.graphservice.service.GraphCacheService;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

// Bounded BFS over the dependencies table. See docs/technical-implementation.md
// section 18 for the algorithm this mirrors, and section 19 for the depth/node
// limits it enforces.
@Service
public class GraphTraversalService {

    private final DependencyRepository dependencyRepository;
    private final ServiceLookupRepository serviceLookupRepository;
    private final GraphTraversalProperties properties;
    private final GraphCacheService graphCacheService;
    private final GraphCacheKeyBuilder cacheKeyBuilder;

    public GraphTraversalService(DependencyRepository dependencyRepository,
                                  ServiceLookupRepository serviceLookupRepository,
                                  GraphTraversalProperties properties,
                                  GraphCacheService graphCacheService,
                                  GraphCacheKeyBuilder cacheKeyBuilder) {
        this.dependencyRepository = dependencyRepository;
        this.serviceLookupRepository = serviceLookupRepository;
        this.properties = properties;
        this.graphCacheService = graphCacheService;
        this.cacheKeyBuilder = cacheKeyBuilder;
    }

    public GraphResponse traverse(UUID rootServiceId, TraversalDirection direction, Integer requestedDepth, String environment) {
        int depth = resolveDepth(requestedDepth);

        String cacheKey = cacheKeyBuilder.build(rootServiceId, direction, environment, depth);
        Optional<GraphResponse> cached = graphCacheService.get(cacheKey);
        if (cached.isPresent()) {
            return cached.get();
        }

        ServiceLookup root = serviceLookupRepository.findById(rootServiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found: " + rootServiceId));

        Map<UUID, GraphNodeDto> nodesById = new LinkedHashMap<>();
        List<GraphEdgeDto> edges = new ArrayList<>();
        Set<UUID> addedDependencyIds = new HashSet<>();
        Map<UUID, Integer> depthById = new HashMap<>();
        Queue<UUID> queue = new ArrayDeque<>();

        nodesById.put(root.getId(), new GraphNodeDto(root.getId(), NodeType.SERVICE, root.getName()));
        depthById.put(root.getId(), 0);
        queue.add(root.getId());

        while (!queue.isEmpty()) {
            UUID currentId = queue.poll();
            int currentDepth = depthById.get(currentId);

            if (currentDepth >= depth) {
                continue;
            }

            if (direction == TraversalDirection.DOWNSTREAM || direction == TraversalDirection.BOTH) {
                for (Dependency dependency : dependencyRepository.findBySourceServiceIdAndEnvironment(currentId, environment)) {
                    visitNeighbor(dependency, dependency.getTargetServiceId(), currentDepth,
                            nodesById, edges, addedDependencyIds, depthById, queue);
                }
            }

            if (direction == TraversalDirection.UPSTREAM || direction == TraversalDirection.BOTH) {
                for (Dependency dependency : dependencyRepository.findByTargetServiceIdAndEnvironment(currentId, environment)) {
                    visitNeighbor(dependency, dependency.getSourceServiceId(), currentDepth,
                            nodesById, edges, addedDependencyIds, depthById, queue);
                }
            }
        }

        GraphResponse response = new GraphResponse(root.getId(), new ArrayList<>(nodesById.values()), edges);
        graphCacheService.put(cacheKey, response);
        return response;
    }

    private void visitNeighbor(Dependency dependency, UUID neighborId, int currentDepth,
                                Map<UUID, GraphNodeDto> nodesById, List<GraphEdgeDto> edges,
                                Set<UUID> addedDependencyIds, Map<UUID, Integer> depthById, Queue<UUID> queue) {
        if (addedDependencyIds.contains(dependency.getId())) {
            return;
        }

        boolean neighborAlreadyKnown = nodesById.containsKey(neighborId);
        if (!neighborAlreadyKnown && nodesById.size() >= properties.getMaxNodes()) {
            // Node budget exhausted - stop growing the graph, but still record edges
            // between nodes we've already discovered.
            return;
        }

        if (!neighborAlreadyKnown) {
            ServiceLookup neighbor = serviceLookupRepository.findById(neighborId).orElse(null);
            String name = neighbor != null ? neighbor.getName() : neighborId.toString();
            nodesById.put(neighborId, new GraphNodeDto(neighborId, NodeType.SERVICE, name));
            depthById.put(neighborId, currentDepth + 1);
            queue.add(neighborId);
        }

        edges.add(new GraphEdgeDto(
                dependency.getSourceServiceId(),
                dependency.getTargetServiceId(),
                parseEnum(DependencyType.class, dependency.getDependencyType()),
                parseEnum(DependencyCriticality.class, dependency.getCriticality()),
                parseEnum(FailureBehavior.class, dependency.getFailureBehavior())
        ));
        addedDependencyIds.add(dependency.getId());
    }

    private int resolveDepth(Integer requestedDepth) {
        int depth = requestedDepth != null ? requestedDepth : properties.getDefaultDepth();
        if (depth < 1 || depth > properties.getMaxDepth()) {
            throw new GraphTraversalLimitExceededException(
                    "Requested depth " + depth + " exceeds the allowed range (1-" + properties.getMaxDepth() + ")");
        }
        return depth;
    }

    private static <E extends Enum<E>> E parseEnum(Class<E> enumType, String value) {
        if (value == null) {
            return null;
        }
        try {
            return Enum.valueOf(enumType, value);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}

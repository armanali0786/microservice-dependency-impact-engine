package com.dependencyimpact.impactanalysis.service;

import com.dependencyimpact.impactanalysis.client.GraphEdgeDto;
import com.dependencyimpact.impactanalysis.client.GraphNodeDto;
import com.dependencyimpact.impactanalysis.client.GraphResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.stream.Collectors;

// The graph service already bounds the traversal by depth - this just re-walks the
// small subgraph it returned to work out, for each node, its shortest distance from
// the root and the path of service names that gets there. That's cheap because the
// subgraph is already small (bounded by depth/maxNodes on the graph-service side).
//
// GraphServiceClient calls the /upstream endpoint, so graph.root() is the changed
// service and every other node is something that (transitively) CALLS it. Edges keep
// their original dependency direction (edge.source() calls edge.target()) regardless
// of which way the graph service walked to find them - so root is only ever an
// edge's target, never its source. That means adjacency has to be built by grouping
// on edge.target(), and a newly-discovered neighbor is always edge.source() - the
// mirror image of a downstream walk (which is what the first version of this class
// did, incorrectly, before this was caught - see GraphServiceClient for the fix).
@Service
public class DependencyPathBuilder {

    public List<TraversedComponent> build(GraphResponse graph) {
        Map<UUID, GraphNodeDto> nodesById = graph.nodes().stream()
                .collect(Collectors.toMap(GraphNodeDto::id, node -> node));

        Map<UUID, List<GraphEdgeDto>> incomingByTarget = new HashMap<>();
        for (GraphEdgeDto edge : graph.edges()) {
            incomingByTarget.computeIfAbsent(edge.target(), k -> new ArrayList<>()).add(edge);
        }

        Map<UUID, Integer> depthById = new HashMap<>();
        Map<UUID, GraphEdgeDto> parentEdgeById = new HashMap<>();
        Queue<UUID> queue = new ArrayDeque<>();

        depthById.put(graph.root(), 0);
        queue.add(graph.root());

        while (!queue.isEmpty()) {
            UUID currentId = queue.poll();
            int currentDepth = depthById.get(currentId);

            for (GraphEdgeDto edge : incomingByTarget.getOrDefault(currentId, List.of())) {
                UUID callerId = edge.source();
                if (depthById.containsKey(callerId)) {
                    continue;
                }
                depthById.put(callerId, currentDepth + 1);
                parentEdgeById.put(callerId, edge);
                queue.add(callerId);
            }
        }

        List<TraversedComponent> components = new ArrayList<>();
        for (UUID serviceId : depthById.keySet()) {
            if (serviceId.equals(graph.root())) {
                continue;
            }
            // This is the actual checkout->payment style edge (the caller's real
            // dependency on whatever it calls next in the chain) - exactly what
            // RiskClassificationService needs to reason about (its criticality,
            // failure behavior) for this component.
            GraphEdgeDto callerEdge = parentEdgeById.get(serviceId);
            components.add(new TraversedComponent(
                    serviceId,
                    nodesById.get(serviceId).name(),
                    depthById.get(serviceId),
                    callerEdge.type(),
                    callerEdge.criticality(),
                    callerEdge.failureBehavior(),
                    buildPath(serviceId, nodesById, parentEdgeById, graph.root())
            ));
        }
        return components;
    }

    private List<String> buildPath(UUID affectedServiceId, Map<UUID, GraphNodeDto> nodesById,
                                    Map<UUID, GraphEdgeDto> parentEdgeById, UUID rootId) {
        List<String> reversePath = new ArrayList<>();
        UUID currentId = affectedServiceId;
        while (currentId != null) {
            reversePath.add(nodesById.get(currentId).name());
            GraphEdgeDto parentEdge = parentEdgeById.get(currentId);
            currentId = parentEdge != null ? parentEdge.target() : null;
            if (currentId != null && currentId.equals(rootId)) {
                reversePath.add(nodesById.get(rootId).name());
                break;
            }
        }
        List<String> path = new ArrayList<>(reversePath);
        Collections.reverse(path);
        return path;
    }
}

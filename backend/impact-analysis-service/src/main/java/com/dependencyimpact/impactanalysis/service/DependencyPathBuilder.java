package com.dependencyimpact.impactanalysis.service;

import com.dependencyimpact.impactanalysis.client.GraphEdgeDto;
import com.dependencyimpact.impactanalysis.client.GraphNodeDto;
import com.dependencyimpact.impactanalysis.client.GraphResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.ArrayList;
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
@Service
public class DependencyPathBuilder {

    public List<TraversedComponent> build(GraphResponse graph) {
        Map<UUID, GraphNodeDto> nodesById = graph.nodes().stream()
                .collect(Collectors.toMap(GraphNodeDto::id, node -> node));

        Map<UUID, List<GraphEdgeDto>> outgoingBySource = new HashMap<>();
        for (GraphEdgeDto edge : graph.edges()) {
            outgoingBySource.computeIfAbsent(edge.source(), k -> new ArrayList<>()).add(edge);
        }

        Map<UUID, Integer> depthById = new HashMap<>();
        Map<UUID, GraphEdgeDto> parentEdgeById = new HashMap<>();
        Queue<UUID> queue = new ArrayDeque<>();

        depthById.put(graph.root(), 0);
        queue.add(graph.root());

        while (!queue.isEmpty()) {
            UUID currentId = queue.poll();
            int currentDepth = depthById.get(currentId);

            for (GraphEdgeDto edge : outgoingBySource.getOrDefault(currentId, List.of())) {
                if (depthById.containsKey(edge.target())) {
                    continue;
                }
                depthById.put(edge.target(), currentDepth + 1);
                parentEdgeById.put(edge.target(), edge);
                queue.add(edge.target());
            }
        }

        List<TraversedComponent> components = new ArrayList<>();
        for (UUID serviceId : depthById.keySet()) {
            if (serviceId.equals(graph.root())) {
                continue;
            }
            GraphEdgeDto edgeIntoNode = parentEdgeById.get(serviceId);
            components.add(new TraversedComponent(
                    serviceId,
                    nodesById.get(serviceId).name(),
                    depthById.get(serviceId),
                    edgeIntoNode.type(),
                    edgeIntoNode.criticality(),
                    edgeIntoNode.failureBehavior(),
                    buildPath(serviceId, nodesById, parentEdgeById, graph.root())
            ));
        }
        return components;
    }

    private List<String> buildPath(UUID targetId, Map<UUID, GraphNodeDto> nodesById,
                                    Map<UUID, GraphEdgeDto> parentEdgeById, UUID rootId) {
        List<String> reversePath = new ArrayList<>();
        UUID currentId = targetId;
        while (currentId != null) {
            reversePath.add(nodesById.get(currentId).name());
            GraphEdgeDto parentEdge = parentEdgeById.get(currentId);
            currentId = parentEdge != null ? parentEdge.source() : null;
            if (currentId != null && currentId.equals(rootId)) {
                reversePath.add(nodesById.get(rootId).name());
                break;
            }
        }
        List<String> path = new ArrayList<>(reversePath);
        java.util.Collections.reverse(path);
        return path;
    }
}

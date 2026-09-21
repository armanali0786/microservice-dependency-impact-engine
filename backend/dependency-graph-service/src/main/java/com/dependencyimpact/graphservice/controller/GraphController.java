package com.dependencyimpact.graphservice.controller;

import com.dependencyimpact.common.model.ApiResponse;
import com.dependencyimpact.graphservice.dto.GraphResponse;
import com.dependencyimpact.graphservice.traversal.GraphTraversalService;
import com.dependencyimpact.graphservice.traversal.TraversalDirection;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class GraphController {

    private final GraphTraversalService graphTraversalService;

    public GraphController(GraphTraversalService graphTraversalService) {
        this.graphTraversalService = graphTraversalService;
    }

    @GetMapping("/api/v1/graph/services/{id}")
    public ResponseEntity<ApiResponse<GraphResponse>> getSubgraph(
            @PathVariable UUID id,
            @RequestParam(required = false) Integer depth,
            @RequestParam(defaultValue = "development") String environment) {
        GraphResponse graph = graphTraversalService.traverse(id, TraversalDirection.BOTH, depth, environment);
        return ResponseEntity.ok(new ApiResponse<>(true, graph, null, null));
    }

    @GetMapping("/api/v1/graph/services/{id}/downstream")
    public ResponseEntity<ApiResponse<GraphResponse>> getDownstream(
            @PathVariable UUID id,
            @RequestParam(required = false) Integer depth,
            @RequestParam(defaultValue = "development") String environment) {
        GraphResponse graph = graphTraversalService.traverse(id, TraversalDirection.DOWNSTREAM, depth, environment);
        return ResponseEntity.ok(new ApiResponse<>(true, graph, null, null));
    }

    @GetMapping("/api/v1/graph/services/{id}/upstream")
    public ResponseEntity<ApiResponse<GraphResponse>> getUpstream(
            @PathVariable UUID id,
            @RequestParam(required = false) Integer depth,
            @RequestParam(defaultValue = "development") String environment) {
        GraphResponse graph = graphTraversalService.traverse(id, TraversalDirection.UPSTREAM, depth, environment);
        return ResponseEntity.ok(new ApiResponse<>(true, graph, null, null));
    }
}

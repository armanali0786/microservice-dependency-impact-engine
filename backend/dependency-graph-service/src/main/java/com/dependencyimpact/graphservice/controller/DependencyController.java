package com.dependencyimpact.graphservice.controller;

import com.dependencyimpact.common.model.ApiResponse;
import com.dependencyimpact.graphservice.dto.DependencyResponse;
import com.dependencyimpact.graphservice.service.DependencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class DependencyController {

    private final DependencyService dependencyService;

    public DependencyController(DependencyService dependencyService) {
        this.dependencyService = dependencyService;
    }

    @GetMapping("/api/v1/dependencies")
    public ResponseEntity<ApiResponse<List<DependencyResponse>>> listAll() {
        return ResponseEntity.ok(new ApiResponse<>(true, dependencyService.listAll(), null, null));
    }

    @GetMapping("/api/v1/services/{id}/dependencies")
    public ResponseEntity<ApiResponse<List<DependencyResponse>>> listDownstream(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "development") String environment) {
        return ResponseEntity.ok(new ApiResponse<>(true, dependencyService.listDownstream(id, environment), null, null));
    }

    @GetMapping("/api/v1/services/{id}/dependents")
    public ResponseEntity<ApiResponse<List<DependencyResponse>>> listUpstream(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "development") String environment) {
        return ResponseEntity.ok(new ApiResponse<>(true, dependencyService.listUpstream(id, environment), null, null));
    }
}

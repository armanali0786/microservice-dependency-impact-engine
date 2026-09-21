package com.dependencyimpact.runtimeanalysis.controller;

import com.dependencyimpact.common.model.ApiResponse;
import com.dependencyimpact.runtimeanalysis.dto.RuntimeMetricsResponse;
import com.dependencyimpact.runtimeanalysis.service.RuntimeMetricsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

@RestController
public class RuntimeMetricsController {

    private final RuntimeMetricsService runtimeMetricsService;

    public RuntimeMetricsController(RuntimeMetricsService runtimeMetricsService) {
        this.runtimeMetricsService = runtimeMetricsService;
    }

    @GetMapping("/api/v1/runtime/services/{id}/metrics")
    public ResponseEntity<ApiResponse<RuntimeMetricsResponse>> getMetrics(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "development") String environment,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
        RuntimeMetricsResponse metrics = runtimeMetricsService.computeMetrics(id, environment, from, to);
        return ResponseEntity.ok(new ApiResponse<>(true, metrics, null, null));
    }
}

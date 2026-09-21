package com.dependencyimpact.runtimeanalysis.controller;

import com.dependencyimpact.common.model.ApiResponse;
import com.dependencyimpact.runtimeanalysis.dto.RuntimeObservationRequest;
import com.dependencyimpact.runtimeanalysis.dto.RuntimeObservationResponse;
import com.dependencyimpact.runtimeanalysis.service.TelemetryIngestionService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
public class RuntimeObservationController {

    private final TelemetryIngestionService telemetryIngestionService;

    public RuntimeObservationController(TelemetryIngestionService telemetryIngestionService) {
        this.telemetryIngestionService = telemetryIngestionService;
    }

    @PostMapping("/api/v1/runtime/observations")
    public ResponseEntity<ApiResponse<RuntimeObservationResponse>> ingest(
            @Valid @RequestBody RuntimeObservationRequest request) {
        RuntimeObservationResponse response = telemetryIngestionService.ingest(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, response, null, null));
    }

    @GetMapping("/api/v1/runtime/services/{id}/observations")
    public ResponseEntity<ApiResponse<List<RuntimeObservationResponse>>> listForService(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "development") String environment,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @RequestParam(required = false) Integer statusCode,
            @RequestParam(required = false) String endpoint) {
        List<RuntimeObservationResponse> observations =
                telemetryIngestionService.listForService(id, environment, from, to, statusCode, endpoint);
        return ResponseEntity.ok(new ApiResponse<>(true, observations, null, null));
    }
}

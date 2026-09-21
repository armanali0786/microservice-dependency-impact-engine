package com.dependencyimpact.runtimeanalysis.controller;

import com.dependencyimpact.common.model.ApiResponse;
import com.dependencyimpact.runtimeanalysis.client.ImpactAnalyzeResponse;
import com.dependencyimpact.runtimeanalysis.dto.CreateIncidentRequest;
import com.dependencyimpact.runtimeanalysis.dto.IncidentImpactResponse;
import com.dependencyimpact.runtimeanalysis.dto.IncidentResponse;
import com.dependencyimpact.runtimeanalysis.dto.IncidentStatusUpdateRequest;
import com.dependencyimpact.runtimeanalysis.service.IncidentService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
public class IncidentController {

    private final IncidentService incidentService;

    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @PostMapping("/api/v1/incidents")
    public ResponseEntity<ApiResponse<IncidentResponse>> createIncident(@Valid @RequestBody CreateIncidentRequest request) {
        IncidentResponse response = incidentService.createIncident(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, response, null, null));
    }

    @GetMapping("/api/v1/incidents")
    public ResponseEntity<ApiResponse<List<IncidentResponse>>> listIncidents(
            @RequestParam(required = false) UUID serviceId,
            @RequestParam(required = false) String severity,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {
        List<IncidentResponse> incidents = incidentService.listIncidents(serviceId, severity, status, from, to);
        return ResponseEntity.ok(new ApiResponse<>(true, incidents, null, null));
    }

    @GetMapping("/api/v1/incidents/{id}")
    public ResponseEntity<ApiResponse<IncidentResponse>> getIncident(@PathVariable UUID id) {
        return ResponseEntity.ok(new ApiResponse<>(true, incidentService.getIncident(id), null, null));
    }

    @PatchMapping("/api/v1/incidents/{id}/status")
    public ResponseEntity<ApiResponse<IncidentResponse>> updateStatus(
            @PathVariable UUID id, @Valid @RequestBody IncidentStatusUpdateRequest request) {
        IncidentResponse response = incidentService.updateStatus(id, request.getStatus());
        return ResponseEntity.ok(new ApiResponse<>(true, response, null, null));
    }

    @PostMapping("/api/v1/incidents/{id}/analyze-impact")
    public ResponseEntity<ApiResponse<ImpactAnalyzeResponse>> analyzeImpact(
            @PathVariable UUID id, @RequestParam(required = false) String environment) {
        ImpactAnalyzeResponse response = incidentService.triggerImpactAnalysis(id, environment);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ApiResponse<>(true, response, null, null));
    }

    @GetMapping("/api/v1/incidents/{id}/impact")
    public ResponseEntity<ApiResponse<IncidentImpactResponse>> getImpact(@PathVariable UUID id) {
        return ResponseEntity.ok(new ApiResponse<>(true, incidentService.getImpact(id), null, null));
    }
}

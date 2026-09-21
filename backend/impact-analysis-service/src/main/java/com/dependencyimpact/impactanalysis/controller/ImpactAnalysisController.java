package com.dependencyimpact.impactanalysis.controller;

import com.dependencyimpact.common.model.ApiResponse;
import com.dependencyimpact.impactanalysis.dto.ImpactAnalysisRequest;
import com.dependencyimpact.impactanalysis.dto.ImpactAnalysisResponse;
import com.dependencyimpact.impactanalysis.dto.ImpactAnalysisStatusResponse;
import com.dependencyimpact.impactanalysis.dto.ImpactComponentResponse;
import com.dependencyimpact.impactanalysis.dto.ImpactReportResponse;
import com.dependencyimpact.impactanalysis.service.ImpactAnalysisService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class ImpactAnalysisController {

    private final ImpactAnalysisService impactAnalysisService;

    public ImpactAnalysisController(ImpactAnalysisService impactAnalysisService) {
        this.impactAnalysisService = impactAnalysisService;
    }

    @PostMapping("/api/v1/impact/analyze")
    public ResponseEntity<ApiResponse<ImpactAnalysisResponse>> analyze(@Valid @RequestBody ImpactAnalysisRequest request) {
        ImpactAnalysisResponse response = impactAnalysisService.startAnalysis(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new ApiResponse<>(true, response, null, null));
    }

    @GetMapping("/api/v1/impact/{id}")
    public ResponseEntity<ApiResponse<ImpactAnalysisStatusResponse>> getStatus(@PathVariable UUID id) {
        return ResponseEntity.ok(new ApiResponse<>(true, impactAnalysisService.getStatus(id), null, null));
    }

    @GetMapping("/api/v1/impact/{id}/report")
    public ResponseEntity<ApiResponse<ImpactReportResponse>> getReport(@PathVariable UUID id) {
        return ResponseEntity.ok(new ApiResponse<>(true, impactAnalysisService.getReport(id), null, null));
    }

    @GetMapping("/api/v1/impact/{id}/components")
    public ResponseEntity<ApiResponse<List<ImpactComponentResponse>>> getComponents(
            @PathVariable UUID id,
            @RequestParam(required = false) String impactLevel,
            @RequestParam(required = false) String dependencyType,
            @RequestParam(required = false) Integer depth) {
        List<ImpactComponentResponse> components =
                impactAnalysisService.listComponents(id, impactLevel, dependencyType, depth);
        return ResponseEntity.ok(new ApiResponse<>(true, components, null, null));
    }
}

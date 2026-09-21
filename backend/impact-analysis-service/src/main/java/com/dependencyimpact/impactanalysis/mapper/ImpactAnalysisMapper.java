package com.dependencyimpact.impactanalysis.mapper;

import com.dependencyimpact.impactanalysis.dto.ImpactAnalysisRequest;
import com.dependencyimpact.impactanalysis.dto.ImpactAnalysisResponse;
import com.dependencyimpact.impactanalysis.dto.ImpactAnalysisStatusResponse;
import com.dependencyimpact.impactanalysis.entity.ImpactAnalysis;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class ImpactAnalysisMapper {

    public ImpactAnalysis newPending(ImpactAnalysisRequest request, UUID id, int resolvedDepth) {
        ImpactAnalysis analysis = new ImpactAnalysis();
        analysis.setId(id);
        analysis.setSourceServiceId(request.getServiceId());
        analysis.setChangeType(request.getChangeType());
        analysis.setEnvironment(request.getEnvironment());
        analysis.setApiId(request.getApiId());
        analysis.setTraversalDepth(resolvedDepth);
        analysis.setStatus("PENDING");
        analysis.setCreatedAt(Instant.now());
        return analysis;
    }

    public ImpactAnalysisResponse toAnalysisResponse(ImpactAnalysis analysis) {
        return new ImpactAnalysisResponse(analysis.getId(), analysis.getStatus());
    }

    public ImpactAnalysisStatusResponse toStatusResponse(ImpactAnalysis analysis) {
        return new ImpactAnalysisStatusResponse(
                analysis.getId(),
                analysis.getStatus(),
                analysis.getRiskLevel(),
                analysis.getStartedAt(),
                analysis.getCompletedAt(),
                analysis.getErrorMessage()
        );
    }
}

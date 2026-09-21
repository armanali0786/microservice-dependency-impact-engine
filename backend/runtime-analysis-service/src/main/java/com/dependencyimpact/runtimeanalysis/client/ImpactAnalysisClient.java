package com.dependencyimpact.runtimeanalysis.client;

import com.dependencyimpact.common.model.ApiResponse;
import com.dependencyimpact.runtimeanalysis.exception.RuntimeAnalysisException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.UUID;

@Component
public class ImpactAnalysisClient {

    private final WebClient webClient;

    public ImpactAnalysisClient(WebClient.Builder webClientBuilder,
                                 @Value("${services.impact-analysis.base-url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public ImpactAnalyzeResponse startAnalysis(UUID serviceId, String changeType, String environment) {
        try {
            ApiResponse<ImpactAnalyzeResponse> response = webClient.post()
                    .uri("/api/v1/impact/analyze")
                    .bodyValue(new ImpactAnalyzeRequest(serviceId, changeType, environment))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<ApiResponse<ImpactAnalyzeResponse>>() {
                    })
                    .block();

            if (response == null || !response.success() || response.data() == null) {
                throw new RuntimeAnalysisException("Impact analysis service returned no data for service " + serviceId);
            }
            return response.data();
        } catch (WebClientResponseException ex) {
            throw new RuntimeAnalysisException(
                    "Impact analysis service call failed for service " + serviceId + ": " + ex.getStatusCode());
        }
    }

    // Returns null if the analysis isn't COMPLETED yet (impact-analysis-service
    // responds 400 IMPACT_ANALYSIS_ERROR in that case) - callers treat that as
    // "not ready yet", not a hard failure.
    public ImpactReportDto getReport(UUID analysisId) {
        try {
            ApiResponse<ImpactReportDto> response = webClient.get()
                    .uri("/api/v1/impact/{id}/report", analysisId)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<ApiResponse<ImpactReportDto>>() {
                    })
                    .block();

            return response != null ? response.data() : null;
        } catch (WebClientResponseException.BadRequest ex) {
            return null;
        } catch (WebClientResponseException ex) {
            throw new RuntimeAnalysisException(
                    "Impact analysis service call failed for analysis " + analysisId + ": " + ex.getStatusCode());
        }
    }
}

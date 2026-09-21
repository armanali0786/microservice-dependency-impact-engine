package com.dependencyimpact.impactanalysis.client;

import com.dependencyimpact.common.model.ApiResponse;
import com.dependencyimpact.impactanalysis.exception.ImpactAnalysisFailedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.UUID;

@Component
public class GraphServiceClient {

    private final WebClient webClient;

    public GraphServiceClient(WebClient.Builder webClientBuilder,
                               @Value("${services.dependency-graph.base-url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    // Blocking on purpose: this runs inside the Kafka consumer thread (ImpactWorker),
    // which is already a background thread doing sequential work - no reactive
    // pipeline needed here, just a synchronous HTTP call.
    public GraphResponse getDownstreamGraph(UUID serviceId, int depth, String environment) {
        try {
            ApiResponse<GraphResponse> response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/v1/graph/services/{id}/downstream")
                            .queryParam("depth", depth)
                            .queryParam("environment", environment)
                            .build(serviceId))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<ApiResponse<GraphResponse>>() {
                    })
                    .block();

            if (response == null || !response.success() || response.data() == null) {
                throw new ImpactAnalysisFailedException(
                        "Graph service returned no data for service " + serviceId);
            }
            return response.data();
        } catch (WebClientResponseException ex) {
            throw new ImpactAnalysisFailedException(
                    "Graph service call failed for service " + serviceId + ": " + ex.getStatusCode());
        }
    }
}

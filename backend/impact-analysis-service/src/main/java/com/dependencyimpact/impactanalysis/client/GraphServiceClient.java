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
    //
    // Deliberately UPSTREAM, not downstream: impact analysis asks "if this service
    // changes/fails, who breaks?" - the answer is whoever depends on it (calls into
    // it), which is upstream in dependency-graph-service's terms (see
    // docs/technical-implementation.md section 20). Downstream would instead answer
    // "what does this service depend on", which is the wrong direction for blast
    // radius - confirmed by docs/api-structure.md section 65's own example
    // (sourceService: payment-service, affected component: checkout-service, i.e.
    // payment's upstream caller, not payment's downstream dependency).
    public GraphResponse getUpstreamGraph(UUID serviceId, int depth, String environment) {
        try {
            ApiResponse<GraphResponse> response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/v1/graph/services/{id}/upstream")
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

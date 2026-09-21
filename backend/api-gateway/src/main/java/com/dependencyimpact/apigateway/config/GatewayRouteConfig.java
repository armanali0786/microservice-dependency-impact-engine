package com.dependencyimpact.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Routes requests to backend services by path, per the resource map in
 * docs/api-structure.md #93. Routes are declared most-specific first, since
 * Spring Cloud Gateway takes the first matching route -
 * {@code /services/{id}/dependencies} belongs to the graph service even
 * though the base {@code /services/**} path belongs to service-registry.
 *
 * {@code /internal/**} (dependency-collector's trusted ingestion endpoint) is
 * deliberately NOT routed here - docs/api-structure.md #79 says external
 * clients must not reach it, so it's simply unreachable through the gateway.
 */
@Configuration
public class GatewayRouteConfig {

    @Value("${services.service-registry.url:http://localhost:8081}")
    private String serviceRegistryUrl;

    @Value("${services.dependency-graph.url:http://localhost:8083}")
    private String dependencyGraphUrl;

    @Value("${services.impact-analysis.url:http://localhost:8084}")
    private String impactAnalysisUrl;

    @Value("${services.runtime-analysis.url:http://localhost:8085}")
    private String runtimeAnalysisUrl;

    @Value("${services.notification.url:http://localhost:8086}")
    private String notificationUrl;

    @Value("${services.notification.ws-url:ws://localhost:8086}")
    private String notificationWsUrl;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                // service-registry: auth, users, teams, audit logs, dashboard, search
                .route("auth", r -> r.path("/api/v1/auth/**").uri(serviceRegistryUrl))
                .route("users", r -> r.path("/api/v1/users/**").uri(serviceRegistryUrl))
                .route("teams", r -> r.path("/api/v1/teams/**").uri(serviceRegistryUrl))
                .route("audit-logs", r -> r.path("/api/v1/audit-logs/**").uri(serviceRegistryUrl))
                .route("dashboard", r -> r.path("/api/v1/dashboard/**").uri(serviceRegistryUrl))
                .route("search", r -> r.path("/api/v1/search/**").uri(serviceRegistryUrl))

                // dependency-graph-service: sub-resources of /services that it owns,
                // declared before the service-registry catch-all below
                .route("service-dependencies", r -> r.path("/api/v1/services/*/dependencies").uri(dependencyGraphUrl))
                .route("service-dependents", r -> r.path("/api/v1/services/*/dependents").uri(dependencyGraphUrl))
                .route("service-apis", r -> r.path("/api/v1/services/*/apis").uri(dependencyGraphUrl))
                .route("service-database-dependencies", r -> r.path("/api/v1/services/*/database-dependencies").uri(dependencyGraphUrl))
                .route("service-health", r -> r.path("/api/v1/services/*/health").uri(runtimeAnalysisUrl))
                .route("service-environments", r -> r.path("/api/v1/services/*/environments").uri(serviceRegistryUrl))

                // service-registry: base service CRUD (catch-all for what's left of /services/**)
                .route("services", r -> r.path("/api/v1/services/**").uri(serviceRegistryUrl))

                // dependency-graph-service: everything else it owns
                .route("apis", r -> r.path("/api/v1/apis/**").uri(dependencyGraphUrl))
                .route("dependencies", r -> r.path("/api/v1/dependencies/**").uri(dependencyGraphUrl))
                .route("graph", r -> r.path("/api/v1/graph/**").uri(dependencyGraphUrl))
                .route("kafka", r -> r.path("/api/v1/kafka/**").uri(dependencyGraphUrl))
                .route("databases", r -> r.path("/api/v1/databases/**").uri(dependencyGraphUrl))
                .route("external-dependencies", r -> r.path("/api/v1/external-dependencies/**").uri(dependencyGraphUrl))

                // impact-analysis-service
                .route("impact", r -> r.path("/api/v1/impact/**").uri(impactAnalysisUrl))

                // runtime-analysis-service
                .route("incidents", r -> r.path("/api/v1/incidents/**").uri(runtimeAnalysisUrl))
                .route("runtime", r -> r.path("/api/v1/runtime/**").uri(runtimeAnalysisUrl))

                // notification-service
                .route("notifications-api", r -> r.path("/api/v1/notifications/**").uri(notificationUrl))
                .route("notifications-ws", r -> r.path("/ws/**").uri(notificationWsUrl))

                .build();
    }
}

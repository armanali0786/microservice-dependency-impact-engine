package com.dependencyimpact.graphservice.cache;

import com.dependencyimpact.graphservice.traversal.TraversalDirection;
import org.springframework.stereotype.Component;

import java.util.UUID;

// Key shape follows docs/implementation-roadmap.md section 10.6
// ("graph:service:{id}:production:depth:2") and
// docs/technical-implementation.md's cache-key example, extended with direction -
// BOTH/UPSTREAM/DOWNSTREAM are different result sets for the same serviceId, so
// direction has to be part of the key or a downstream lookup could return a cached
// upstream result (the exact class of bug already caught once this session in
// impact-analysis-service's traversal direction).
@Component
public class GraphCacheKeyBuilder {

    public String build(UUID serviceId, TraversalDirection direction, String environment, int depth) {
        return "graph:" + direction.name().toLowerCase() + ":" + serviceId + ":" + environment + ":depth:" + depth;
    }
}

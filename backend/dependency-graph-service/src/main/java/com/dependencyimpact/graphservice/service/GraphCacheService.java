package com.dependencyimpact.graphservice.service;

import com.dependencyimpact.graphservice.dto.GraphResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

// TTL-only caching, no active invalidation - a newly-discovered dependency won't be
// reflected in a cached subgraph until the entry expires (default 60s). That's a
// deliberate simplification: proactively invalidating on every dependency write
// would mean tracking which cache keys any given service/environment could appear
// in, which is real complexity for a learning MVP's traffic volume. Flagged, not
// hidden - see docs/implementation-roadmap.md section 10.6.
@Service
public class GraphCacheService {

    private static final Logger log = LoggerFactory.getLogger(GraphCacheService.class);

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final Duration ttl;

    public GraphCacheService(StringRedisTemplate redisTemplate, ObjectMapper objectMapper,
                              @Value("${graph.cache.ttl-seconds:60}") long ttlSeconds) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.ttl = Duration.ofSeconds(ttlSeconds);
    }

    public Optional<GraphResponse> get(String key) {
        try {
            String json = redisTemplate.opsForValue().get(key);
            if (json == null) {
                return Optional.empty();
            }
            return Optional.of(objectMapper.readValue(json, GraphResponse.class));
        } catch (Exception e) {
            // Redis being unavailable, or a value that fails to deserialize,
            // degrades to a cache miss rather than failing the request - the graph
            // is always recomputable from Postgres, so caching is purely a
            // performance optimization, never a correctness dependency.
            log.warn("Graph cache read failed for key {}, falling back to a live traversal: {}", key, e.getMessage());
            return Optional.empty();
        }
    }

    public void put(String key, GraphResponse graph) {
        try {
            redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(graph), ttl);
        } catch (Exception e) {
            log.warn("Graph cache write failed for key {}: {}", key, e.getMessage());
        }
    }
}

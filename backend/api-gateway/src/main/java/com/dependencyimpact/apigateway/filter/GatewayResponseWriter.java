package com.dependencyimpact.apigateway.filter;

import com.dependencyimpact.common.exceptions.ErrorCode;
import com.dependencyimpact.common.model.ApiError;
import com.dependencyimpact.common.model.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * Short-circuits a Gateway {@code GlobalFilter} with an ApiResponse-shaped
 * JSON error body.
 *
 * <p>Spring Cloud Gateway routes are dispatched through a plain
 * {@code WebHandler}, not an {@code @Controller}, so an exception thrown from
 * a GlobalFilter never reaches {@code @RestControllerAdvice} - it falls
 * through to WebFlux's generic default error body instead. Filters that need
 * to reject a request write the response directly, here, rather than
 * throwing.
 */
final class GatewayResponseWriter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private GatewayResponseWriter() {
    }

    static Mono<Void> writeError(ServerHttpResponse response, HttpStatus status, ErrorCode code, String message) {
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ApiResponse<Void> body = new ApiResponse<>(false, null, new ApiError(code.name(), message, null), null);
        byte[] bytes;
        try {
            bytes = OBJECT_MAPPER.writeValueAsBytes(body);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            bytes = ("{\"success\":false,\"error\":{\"code\":\"" + code.name() + "\"}}").getBytes(StandardCharsets.UTF_8);
        }

        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }
}

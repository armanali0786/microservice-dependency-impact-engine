package com.dependencyimpact.apigateway.exception;

import com.dependencyimpact.common.exceptions.AccessDeniedException;
import com.dependencyimpact.common.exceptions.BaseException;
import com.dependencyimpact.common.exceptions.ErrorCode;
import com.dependencyimpact.common.model.ApiError;
import com.dependencyimpact.common.model.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

/**
 * Catches exceptions raised by the gateway's own filters (e.g. a rejected
 * JWT) so they come back as the same ApiResponse/ApiError shape every other
 * service uses, rather than WebFlux's default error body. This does not see
 * errors from proxying to a downstream service that is down/unreachable -
 * those are handled by Spring Cloud Gateway's own routing error handling.
 */
@RestControllerAdvice
public class GatewayExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GatewayExceptionHandler.class);

    @ExceptionHandler(GatewayAuthenticationException.class)
    public Mono<ResponseEntity<ApiResponse<Void>>> handleAuthentication(GatewayAuthenticationException ex) {
        return error(HttpStatus.UNAUTHORIZED, ErrorCode.AUTHENTICATION_REQUIRED, ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Mono<ResponseEntity<ApiResponse<Void>>> handleAccessDenied(AccessDeniedException ex) {
        return error(HttpStatus.FORBIDDEN, ErrorCode.ACCESS_DENIED, ex.getMessage());
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public Mono<ResponseEntity<ApiResponse<Void>>> handleRateLimit(RateLimitExceededException ex) {
        return error(HttpStatus.TOO_MANY_REQUESTS, ErrorCode.RATE_LIMIT_EXCEEDED, ex.getMessage());
    }

    @ExceptionHandler(BaseException.class)
    public Mono<ResponseEntity<ApiResponse<Void>>> handleBase(BaseException ex) {
        return error(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ApiResponse<Void>>> handleUnexpected(Exception ex) {
        log.error("Unhandled gateway exception", ex);
        return error(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_ERROR, "An unexpected error occurred.");
    }

    private Mono<ResponseEntity<ApiResponse<Void>>> error(HttpStatus status, ErrorCode code, String message) {
        return Mono.just(ResponseEntity.status(status)
                .body(new ApiResponse<>(false, null, new ApiError(code.name(), message, null), null)));
    }
}

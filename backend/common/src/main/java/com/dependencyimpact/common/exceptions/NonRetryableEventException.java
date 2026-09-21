package com.dependencyimpact.common.exceptions;

// Marker for Kafka consumer failures that retrying will never fix - malformed JSON,
// an unparseable envelope, a payload that doesn't match its declared shape. Consumers
// throw this (instead of logging-and-returning) so the container-level error handler
// routes it straight to the DLQ without wasting retry attempts on it, per
// docs/kafka-spec.md section 34 ("Non-Retryable Errors").
//
// Deliberately NOT a BaseException subclass: BaseException is for API-facing errors
// an @ExceptionHandler turns into an HTTP response. This is a Kafka-internal signal
// consumed by DefaultErrorHandler.addNotRetryableExceptions(), a different boundary.
public class NonRetryableEventException extends RuntimeException {
    public NonRetryableEventException(String message, Throwable cause) {
        super(message, cause);
    }

    public NonRetryableEventException(String message) {
        super(message);
    }
}

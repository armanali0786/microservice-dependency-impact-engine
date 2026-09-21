package com.dependencyimpact.runtimeanalysis.exception;

import com.dependencyimpact.common.exceptions.ResourceNotFoundException;
import com.dependencyimpact.common.model.ApiError;
import com.dependencyimpact.common.model.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RuntimeAnalysisExceptionHandler {

    @ExceptionHandler(IncidentNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleIncidentNotFound(IncidentNotFoundException ex) {
        ApiError error = new ApiError("INCIDENT_NOT_FOUND", ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, null, error, null));
    }

    // Fallback for any other not-yet-specifically-handled 404 (e.g. an unknown
    // serviceId on ingestion/incident creation).
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFound(ResourceNotFoundException ex) {
        ApiError error = new ApiError("NOT_FOUND", ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, null, error, null));
    }

    @ExceptionHandler(RuntimeAnalysisException.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneric(RuntimeAnalysisException ex) {
        ApiError error = new ApiError("RUNTIME_ANALYSIS_ERROR", ex.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, null, error, null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, Object> details = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(fieldError -> details.put(fieldError.getField(), fieldError.getDefaultMessage()));

        ApiError error = new ApiError("VALIDATION_FAILED", "Request validation failed", details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, null, error, null));
    }
}

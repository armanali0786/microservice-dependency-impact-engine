package com.dependencyimpact.common.model;

public record ApiError(String code, String message, java.util.Map<String, Object> details) {
}

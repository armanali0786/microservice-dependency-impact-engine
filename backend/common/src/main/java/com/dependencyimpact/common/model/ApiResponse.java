package com.dependencyimpact.common.model;

public record ApiResponse<T>(boolean success, T data, ApiError error, ApiMeta meta) {
}

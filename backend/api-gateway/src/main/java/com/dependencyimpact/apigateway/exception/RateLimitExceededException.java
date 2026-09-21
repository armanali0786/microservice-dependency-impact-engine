package com.dependencyimpact.apigateway.exception;

import com.dependencyimpact.common.exceptions.BaseException;

public class RateLimitExceededException extends BaseException {
    public RateLimitExceededException(String message) {
        super(message);
    }
}

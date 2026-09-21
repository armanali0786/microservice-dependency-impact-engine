package com.dependencyimpact.apigateway.exception;

import com.dependencyimpact.common.exceptions.BaseException;

public class GatewayAuthenticationException extends BaseException {
    public GatewayAuthenticationException(String message) {
        super(message);
    }
}

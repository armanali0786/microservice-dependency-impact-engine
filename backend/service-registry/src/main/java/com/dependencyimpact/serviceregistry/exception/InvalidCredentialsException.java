package com.dependencyimpact.serviceregistry.exception;

import com.dependencyimpact.common.exceptions.BaseException;

public class InvalidCredentialsException extends BaseException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}

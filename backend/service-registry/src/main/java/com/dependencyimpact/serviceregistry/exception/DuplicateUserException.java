package com.dependencyimpact.serviceregistry.exception;

import com.dependencyimpact.common.exceptions.ConflictException;

public class DuplicateUserException extends ConflictException {
    public DuplicateUserException(String message) {
        super(message);
    }
}

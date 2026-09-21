package com.dependencyimpact.graphservice.exception;

import com.dependencyimpact.common.exceptions.ConflictException;

public class DuplicateDependencyException extends ConflictException {
    public DuplicateDependencyException(String message) {
        super(message);
    }
}

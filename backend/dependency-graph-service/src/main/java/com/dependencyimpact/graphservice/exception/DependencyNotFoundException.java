package com.dependencyimpact.graphservice.exception;

import com.dependencyimpact.common.exceptions.ResourceNotFoundException;

public class DependencyNotFoundException extends ResourceNotFoundException {
    public DependencyNotFoundException(String message) {
        super(message);
    }
}

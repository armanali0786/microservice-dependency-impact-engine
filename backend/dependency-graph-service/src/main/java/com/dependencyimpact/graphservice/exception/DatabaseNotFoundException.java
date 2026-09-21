package com.dependencyimpact.graphservice.exception;

import com.dependencyimpact.common.exceptions.ResourceNotFoundException;

public class DatabaseNotFoundException extends ResourceNotFoundException {
    public DatabaseNotFoundException(String message) {
        super(message);
    }
}

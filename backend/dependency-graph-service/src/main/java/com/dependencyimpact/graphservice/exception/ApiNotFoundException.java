package com.dependencyimpact.graphservice.exception;

import com.dependencyimpact.common.exceptions.ResourceNotFoundException;

public class ApiNotFoundException extends ResourceNotFoundException {
    public ApiNotFoundException(String message) {
        super(message);
    }
}

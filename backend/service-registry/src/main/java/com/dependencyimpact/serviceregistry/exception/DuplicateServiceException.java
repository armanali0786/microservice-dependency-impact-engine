package com.dependencyimpact.serviceregistry.exception;

import com.dependencyimpact.common.exceptions.ResourceNotFoundException;

public class DuplicateServiceException extends com.dependencyimpact.common.exceptions.ResourceNotFoundException {
    public DuplicateServiceException(String message) {
        super(message);
    }
}

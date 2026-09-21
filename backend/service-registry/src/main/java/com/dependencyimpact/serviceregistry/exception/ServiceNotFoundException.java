package com.dependencyimpact.serviceregistry.exception;

import com.dependencyimpact.common.exceptions.ResourceNotFoundException;

public class ServiceNotFoundException extends com.dependencyimpact.common.exceptions.ResourceNotFoundException {
    public ServiceNotFoundException(String message) {
        super(message);
    }
}

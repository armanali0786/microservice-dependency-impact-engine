package com.dependencyimpact.serviceregistry.exception;

import com.dependencyimpact.common.exceptions.ResourceNotFoundException;

public class TeamNotFoundException extends com.dependencyimpact.common.exceptions.ResourceNotFoundException {
    public TeamNotFoundException(String message) {
        super(message);
    }
}

package com.dependencyimpact.serviceregistry.exception;

import com.dependencyimpact.common.exceptions.ResourceNotFoundException;

public class UserNotFoundException extends com.dependencyimpact.common.exceptions.ResourceNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}

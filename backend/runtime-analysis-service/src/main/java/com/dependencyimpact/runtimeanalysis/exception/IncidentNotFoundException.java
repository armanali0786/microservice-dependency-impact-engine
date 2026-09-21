package com.dependencyimpact.runtimeanalysis.exception;

import com.dependencyimpact.common.exceptions.ResourceNotFoundException;

public class IncidentNotFoundException extends ResourceNotFoundException {
    public IncidentNotFoundException(String message) {
        super(message);
    }
}

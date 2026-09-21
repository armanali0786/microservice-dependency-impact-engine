package com.dependencyimpact.graphservice.exception;

import com.dependencyimpact.common.exceptions.ResourceNotFoundException;

public class TopicNotFoundException extends ResourceNotFoundException {
    public TopicNotFoundException(String message) {
        super(message);
    }
}

package com.dependencyimpact.graphservice.exception;

import com.dependencyimpact.common.exceptions.ConflictException;

public class DuplicateTopicException extends ConflictException {
    public DuplicateTopicException(String message) {
        super(message);
    }
}

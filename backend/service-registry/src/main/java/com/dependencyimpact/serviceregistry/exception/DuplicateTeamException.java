package com.dependencyimpact.serviceregistry.exception;

import com.dependencyimpact.common.exceptions.ConflictException;

public class DuplicateTeamException extends ConflictException {
    public DuplicateTeamException(String message) {
        super(message);
    }
}

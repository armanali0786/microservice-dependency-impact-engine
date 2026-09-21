package com.dependencyimpact.graphservice.exception;

public class GraphTraversalLimitExceededException extends GraphTraversalException {
    public GraphTraversalLimitExceededException(String message) {
        super(message);
    }
}

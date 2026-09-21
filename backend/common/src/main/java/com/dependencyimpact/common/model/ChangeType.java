package com.dependencyimpact.common.model;

public enum ChangeType {
    SERVICE_CHANGE,
    API_CHANGE,
    API_SCHEMA_CHANGE,
    KAFKA_SCHEMA_CHANGE,
    DATABASE_SCHEMA_CHANGE,
    DEPLOYMENT,
    CONFIGURATION_CHANGE,
    FAILURE;
}

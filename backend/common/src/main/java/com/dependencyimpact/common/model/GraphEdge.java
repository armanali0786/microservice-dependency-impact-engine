package com.dependencyimpact.common.model;

public record GraphEdge(java.util.UUID source, java.util.UUID target, EdgeType type, DependencyCriticality criticality, FailureBehavior failureBehavior) {
}

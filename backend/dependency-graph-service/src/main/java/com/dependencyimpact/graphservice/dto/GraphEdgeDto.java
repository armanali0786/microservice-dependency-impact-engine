package com.dependencyimpact.graphservice.dto;

import com.dependencyimpact.common.model.DependencyCriticality;
import com.dependencyimpact.common.model.DependencyType;
import com.dependencyimpact.common.model.FailureBehavior;

import java.util.UUID;

public record GraphEdgeDto(
        UUID source,
        UUID target,
        DependencyType type,
        DependencyCriticality criticality,
        FailureBehavior failureBehavior
) {
}

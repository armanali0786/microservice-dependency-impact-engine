package com.dependencyimpact.impactanalysis.service;

import com.dependencyimpact.common.model.DependencyCriticality;
import com.dependencyimpact.common.model.DependencyType;
import com.dependencyimpact.common.model.FailureBehavior;

import java.util.List;
import java.util.UUID;

public record TraversedComponent(
        UUID serviceId,
        String serviceName,
        int depth,
        DependencyType dependencyType,
        DependencyCriticality criticality,
        FailureBehavior failureBehavior,
        List<String> dependencyPath
) {
}

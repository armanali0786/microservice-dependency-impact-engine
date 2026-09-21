package com.dependencyimpact.dependencycollector.collector;

import com.dependencyimpact.common.model.DependencyObservation;

public interface DependencyCollector {
    java.util.List<com.dependencyimpact.common.model.DependencyObservation> collect(CollectionContext context);
}

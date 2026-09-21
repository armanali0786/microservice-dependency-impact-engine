package com.dependencyimpact.dependencycollector.collector;

import com.dependencyimpact.common.model.DependencyObservation;

import java.util.List;

@org.springframework.stereotype.Component
public class DatabaseCollector implements DependencyCollector {

    // Real discovery logic (parsing OpenAPI specs / Kafka metadata / DB
    // catalogs / K8s manifests / OTel spans) is a later milestone. For now
    // this collector contributes nothing, and the manual internal endpoint
    // (InternalDependencyEventController) is used to publish observations.
    @Override
    public List<DependencyObservation> collect(CollectionContext context) {
        return List.of();
    }
}

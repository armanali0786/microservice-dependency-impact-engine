package com.dependencyimpact.impactanalysis.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

// Same simplification as dependency-graph-service's ServiceLookup: a direct
// read-only view onto the shared services table, used only to resolve the root
// service's own name for a report. Fine for this MVP's single shared Postgres;
// a stricter setup would call service-registry over HTTP instead.
@Entity
@Table(name = "services")
public class ServiceLookup {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

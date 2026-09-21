package com.dependencyimpact.runtimeanalysis.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

// Same simplification as the other services' ServiceLookup: a read-only view onto
// the shared services table, used here to validate a serviceId before insert (the
// FK-violation-as-500 bug already caught and fixed once in impact-analysis-service)
// and to resolve service names for incident/observation responses.
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

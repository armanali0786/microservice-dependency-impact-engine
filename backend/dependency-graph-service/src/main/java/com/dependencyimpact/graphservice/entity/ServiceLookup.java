package com.dependencyimpact.graphservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

// Read-only view onto service-registry's "services" table, just to resolve a
// service name to its UUID when consuming dependency-events. In a stricter
// microservices setup this would be an HTTP call to service-registry instead;
// this MVP shares one Postgres instance, so a direct read is simpler for now.
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

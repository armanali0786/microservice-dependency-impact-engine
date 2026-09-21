package com.dependencyimpact.graphservice.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "databases")
public class Database {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "database_type")
    private String databaseType;

    @Column(name = "host_identifier")
    private String hostIdentifier;

    @Column(name = "environment")
    private String environment;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}

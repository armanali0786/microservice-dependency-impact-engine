package com.dependencyimpact.serviceregistry.dto;

import java.util.List;
import java.util.UUID;

public class CurrentUserResponse {

    private final UUID id;
    private final String email;
    private final String fullName;
    private final List<String> roles;

    public CurrentUserResponse(UUID id, String email, String fullName, List<String> roles) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.roles = roles;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public List<String> getRoles() {
        return roles;
    }
}

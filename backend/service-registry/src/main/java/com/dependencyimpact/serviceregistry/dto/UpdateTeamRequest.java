package com.dependencyimpact.serviceregistry.dto;

import jakarta.validation.constraints.Email;

public class UpdateTeamRequest {

    private String name;

    private String description;

    @Email(message = "ownerEmail must be a valid email address")
    private String ownerEmail;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }
}

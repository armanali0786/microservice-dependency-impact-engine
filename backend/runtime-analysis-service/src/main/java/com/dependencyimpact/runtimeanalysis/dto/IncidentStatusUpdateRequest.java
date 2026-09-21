package com.dependencyimpact.runtimeanalysis.dto;

import jakarta.validation.constraints.NotBlank;

public class IncidentStatusUpdateRequest {

    @NotBlank
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

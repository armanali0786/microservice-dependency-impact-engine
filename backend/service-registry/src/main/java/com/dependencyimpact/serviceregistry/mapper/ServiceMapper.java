package com.dependencyimpact.serviceregistry.mapper;

import com.dependencyimpact.serviceregistry.dto.CreateServiceRequest;
import com.dependencyimpact.serviceregistry.dto.ServiceResponse;
import com.dependencyimpact.serviceregistry.entity.Service;
import org.springframework.stereotype.Component;

@Component
public class ServiceMapper {

    public Service toEntity(CreateServiceRequest request) {
        Service service = new Service();
        service.setTeamId(request.getTeamId());
        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setTechnology(request.getTechnology());
        service.setRepositoryUrl(request.getRepositoryUrl());
        service.setRepositoryName(request.getRepositoryName());
        service.setVersion(request.getVersion());
        return service;
    }

    public ServiceResponse toResponse(Service service) {
        return new ServiceResponse(
                service.getId(),
                service.getTeamId(),
                service.getName(),
                service.getDescription(),
                service.getTechnology(),
                service.getRepositoryUrl(),
                service.getRepositoryName(),
                service.getStatus(),
                service.getVersion(),
                service.getCreatedAt(),
                service.getUpdatedAt()
        );
    }
}

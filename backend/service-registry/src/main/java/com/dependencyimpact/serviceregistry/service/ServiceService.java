package com.dependencyimpact.serviceregistry.service;

import com.dependencyimpact.serviceregistry.dto.CreateServiceRequest;
import com.dependencyimpact.serviceregistry.dto.ServiceResponse;
import com.dependencyimpact.serviceregistry.dto.UpdateServiceRequest;
import com.dependencyimpact.serviceregistry.entity.Service;
import com.dependencyimpact.serviceregistry.exception.DuplicateServiceException;
import com.dependencyimpact.serviceregistry.exception.ServiceNotFoundException;
import com.dependencyimpact.serviceregistry.exception.TeamNotFoundException;
import com.dependencyimpact.serviceregistry.mapper.ServiceMapper;
import com.dependencyimpact.serviceregistry.repository.ServiceRepository;
import com.dependencyimpact.serviceregistry.repository.TeamRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@org.springframework.stereotype.Service
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final TeamRepository teamRepository;
    private final ServiceMapper serviceMapper;

    public ServiceService(ServiceRepository serviceRepository, TeamRepository teamRepository, ServiceMapper serviceMapper) {
        this.serviceRepository = serviceRepository;
        this.teamRepository = teamRepository;
        this.serviceMapper = serviceMapper;
    }

    public ServiceResponse createService(CreateServiceRequest request) {
        if (!teamRepository.existsById(request.getTeamId())) {
            throw new TeamNotFoundException("Team not found: " + request.getTeamId());
        }
        serviceRepository.findByName(request.getName()).ifPresent(existing -> {
            throw new DuplicateServiceException("A service named '" + request.getName() + "' already exists");
        });

        Service service = serviceMapper.toEntity(request);
        Instant now = Instant.now();
        service.setId(UUID.randomUUID());
        service.setStatus("ACTIVE");
        service.setCreatedAt(now);
        service.setUpdatedAt(now);

        Service saved = serviceRepository.save(service);
        return serviceMapper.toResponse(saved);
    }

    public List<ServiceResponse> listServices() {
        return serviceRepository.findAll().stream()
                .map(serviceMapper::toResponse)
                .toList();
    }

    public ServiceResponse getService(UUID id) {
        return serviceMapper.toResponse(findServiceOrThrow(id));
    }

    public ServiceResponse updateService(UUID id, UpdateServiceRequest request) {
        Service service = findServiceOrThrow(id);

        if (request.getDescription() != null) {
            service.setDescription(request.getDescription());
        }
        if (request.getTechnology() != null) {
            service.setTechnology(request.getTechnology());
        }
        if (request.getRepositoryUrl() != null) {
            service.setRepositoryUrl(request.getRepositoryUrl());
        }
        if (request.getRepositoryName() != null) {
            service.setRepositoryName(request.getRepositoryName());
        }
        if (request.getVersion() != null) {
            service.setVersion(request.getVersion());
        }
        if (request.getStatus() != null) {
            service.setStatus(request.getStatus());
        }
        service.setUpdatedAt(Instant.now());

        return serviceMapper.toResponse(serviceRepository.save(service));
    }

    public void deleteService(UUID id) {
        serviceRepository.delete(findServiceOrThrow(id));
    }

    private Service findServiceOrThrow(UUID id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found: " + id));
    }
}

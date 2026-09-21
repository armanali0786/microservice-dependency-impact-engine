package com.dependencyimpact.graphservice.service;

import com.dependencyimpact.graphservice.dto.DependencyResponse;
import com.dependencyimpact.graphservice.mapper.DependencyMapper;
import com.dependencyimpact.graphservice.repository.DependencyRepository;

import java.util.List;
import java.util.UUID;

@org.springframework.stereotype.Service
public class DependencyService {

    private final DependencyRepository dependencyRepository;
    private final DependencyMapper dependencyMapper;

    public DependencyService(DependencyRepository dependencyRepository, DependencyMapper dependencyMapper) {
        this.dependencyRepository = dependencyRepository;
        this.dependencyMapper = dependencyMapper;
    }

    public List<DependencyResponse> listAll() {
        return dependencyRepository.findAll().stream()
                .map(dependencyMapper::toResponse)
                .toList();
    }

    public List<DependencyResponse> listDownstream(UUID sourceServiceId, String environment) {
        return dependencyRepository.findBySourceServiceIdAndEnvironment(sourceServiceId, environment).stream()
                .map(dependencyMapper::toResponse)
                .toList();
    }

    public List<DependencyResponse> listUpstream(UUID targetServiceId, String environment) {
        return dependencyRepository.findByTargetServiceIdAndEnvironment(targetServiceId, environment).stream()
                .map(dependencyMapper::toResponse)
                .toList();
    }
}

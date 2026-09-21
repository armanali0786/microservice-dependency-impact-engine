package com.dependencyimpact.serviceregistry.controller;

import com.dependencyimpact.common.model.ApiResponse;
import com.dependencyimpact.serviceregistry.dto.CreateServiceRequest;
import com.dependencyimpact.serviceregistry.dto.ServiceResponse;
import com.dependencyimpact.serviceregistry.dto.UpdateServiceRequest;
import com.dependencyimpact.serviceregistry.service.ServiceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/services")
public class ServiceController {

    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ServiceResponse>> createService(@Valid @RequestBody CreateServiceRequest request) {
        ServiceResponse response = serviceService.createService(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, response, null, null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ServiceResponse>>> listServices() {
        return ResponseEntity.ok(new ApiResponse<>(true, serviceService.listServices(), null, null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ServiceResponse>> getService(@PathVariable UUID id) {
        return ResponseEntity.ok(new ApiResponse<>(true, serviceService.getService(id), null, null));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<ServiceResponse>> updateService(@PathVariable UUID id,
                                                                        @Valid @RequestBody UpdateServiceRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(true, serviceService.updateService(id, request), null, null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable UUID id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}

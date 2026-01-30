package com.solution.coreservice.controller;

import com.solution.coreservice.dto.request.ServiceConfigRequest;
import com.solution.coreservice.dto.response.ServiceConfigResponse;
import com.solution.coreservice.dto.response.ServiceConfigShortResponse;
import com.solution.coreservice.service.ServiceConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/service-configs")
public class ServiceConfigController {

    private final ServiceConfigService serviceConfigService;

    @GetMapping("/{id}")
    public ResponseEntity<ServiceConfigResponse> get(
            @RequestHeader("X-User-Id") UUID userId,
            @PathVariable String id
    ) {
        return ResponseEntity.ok(serviceConfigService.get(userId, id));
    }

    @GetMapping
    public ResponseEntity<List<ServiceConfigShortResponse>> getAll(
            @RequestHeader("X-User-Id") UUID userId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(serviceConfigService.getAll(userId, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceConfigResponse> update(
            @RequestHeader("X-User-Id") UUID userId,
            @PathVariable String id,
            @Valid @RequestBody ServiceConfigRequest serviceConfigRequest
    ) {
        return ResponseEntity.ok(serviceConfigService.update(userId, serviceConfigRequest, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceConfig(
            @RequestHeader("X-User-Id") UUID userId,
            @PathVariable String id
    ) {
        serviceConfigService.delete(userId, id);
        return ResponseEntity.noContent().build();
    }
}

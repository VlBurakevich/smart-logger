package com.solution.coreservice.controller;

import com.solution.coreservice.dto.request.MonitoringTaskCreateRequest;
import com.solution.coreservice.dto.request.MonitoringTaskUpdateRequest;
import com.solution.coreservice.dto.response.MonitoringTaskResponse;
import com.solution.coreservice.dto.response.MonitoringTaskShortResponse;
import com.solution.coreservice.service.MonitoringTaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/core/monitoring-tasks")
public class MonitoringTaskController {
    private final MonitoringTaskService monitoringTaskService;

    @GetMapping("/{id}")
    public ResponseEntity<MonitoringTaskResponse> get(
            @RequestHeader("User-Id") UUID userId,
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(monitoringTaskService.get(userId, id));
    }

    @GetMapping
    public ResponseEntity<Page<MonitoringTaskShortResponse>> getAll(
            @RequestHeader("User-Id") UUID userId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(monitoringTaskService.getAll(userId, pageable));
    }

    @PostMapping
    public ResponseEntity<MonitoringTaskResponse> create(
            @RequestHeader("User-Id") UUID userId,
            @RequestBody @Valid MonitoringTaskCreateRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(monitoringTaskService.create(request, userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MonitoringTaskResponse> update(
            @RequestHeader("User-Id") UUID userId,
            @PathVariable UUID id,
            @RequestBody @Valid MonitoringTaskUpdateRequest request
    ) {
        return ResponseEntity.ok(monitoringTaskService.update(userId, request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceConfig(
            @RequestHeader("User-Id") UUID userId,
            @PathVariable UUID id
    ) {
        monitoringTaskService.delete(userId, id);
        return ResponseEntity.noContent().build();
    }
}

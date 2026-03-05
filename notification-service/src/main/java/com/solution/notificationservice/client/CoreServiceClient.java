package com.solution.notificationservice.client;

import com.solution.notificationservice.dto.ServiceNamesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "core-service")
public interface CoreServiceClient {
    @GetMapping("/api/core/internal/users/{userId}/services")
    ServiceNamesResponse getServiceNames(@PathVariable UUID userId);
}

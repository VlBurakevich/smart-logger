package com.solution.authservice.client;

import com.solution.authservice.dto.request.RegisterCoreRequest;
import com.solution.authservice.dto.request.StatusUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "core-service")
public interface CoreServiceClient {

    @PostMapping("/api/core/internal/account/register")
    void register(@RequestBody RegisterCoreRequest registerCoreRequest);

    @PutMapping("/api/core/internal/account/{id}/status")
    void setIsActive(
            @PathVariable UUID id,
            @RequestBody StatusUpdateRequest statusUpdateRequest
    );

    @DeleteMapping("/api/core/internal/account/{id}")
    void deleteUserById(@PathVariable UUID id);
}

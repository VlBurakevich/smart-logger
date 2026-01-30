package com.solution.authservice.client;

import com.solution.authservice.dto.request.RegisterCoreRequest;
import com.solution.authservice.dto.request.StatusUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "core-service")
public interface CoreServiceClient {

    @PostMapping("/api/internal/account/register")
    void register(@RequestBody RegisterCoreRequest registerCoreRequest);

    @PutMapping("/api/internal/account/{userId}/status")
    void setIsActive(
            @PathVariable UUID userId,
            @RequestBody StatusUpdateRequest statusUpdateRequest
    );

    @DeleteMapping("/api/internal/account/{userId}")
    void deleteUserById(@PathVariable UUID userId);
}

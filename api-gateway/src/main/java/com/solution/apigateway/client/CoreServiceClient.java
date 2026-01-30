package com.solution.apigateway.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "core-service")
public interface CoreServiceClient {

    @GetMapping("/api/internal/apiKey/exists")
    boolean apiKeyExists(@RequestHeader("X-Api-Key-Hash") String apiKey);
}

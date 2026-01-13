package com.solution.apigateway.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "core-service")
public interface CoreServiceClient {

    @GetMapping("/api/internal/accounts/exists")
    boolean accountExists(@RequestParam("accountId") String accountId);
}

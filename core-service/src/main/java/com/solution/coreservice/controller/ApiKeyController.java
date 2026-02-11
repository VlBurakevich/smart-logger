package com.solution.coreservice.controller;

import com.solution.coreservice.dto.request.ApiKeyCreateRequest;
import com.solution.coreservice.dto.response.ApiKeyInfoResponse;
import com.solution.coreservice.dto.response.ApiKeyResponse;
import com.solution.coreservice.service.ApiKeyService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/keys")
public class ApiKeyController {
    private final ApiKeyService apiKeyService;

    @GetMapping()
    public ResponseEntity<Page<ApiKeyInfoResponse>> getUserKeys(
        @RequestHeader("User-Id") UUID userId,
        Pageable pageable
    ) {
        return ResponseEntity.ok(apiKeyService.getUserKeys(userId, pageable));
    }

    @PostMapping()
    public ResponseEntity<ApiKeyResponse> createNewKey(
        @RequestHeader("User-Id") UUID userId,
        @RequestBody @Valid ApiKeyCreateRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(apiKeyService.createNewKey(userId, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKey(
        @RequestHeader("User-Id") UUID userId,
        @PathVariable UUID id
    ) {
        apiKeyService.deleteKey(userId, id);
        return ResponseEntity.noContent().build();
    }
}



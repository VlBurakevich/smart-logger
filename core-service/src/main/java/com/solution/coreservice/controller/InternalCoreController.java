package com.solution.coreservice.controller;

import com.solution.coreservice.dto.request.RegisterRequest;
import com.solution.coreservice.dto.request.StatusUpdateRequest;
import com.solution.coreservice.service.ApiKeyService;
import com.solution.coreservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/core/internal")
public class InternalCoreController {
    private final ApiKeyService apiKeyService;
    private final UserService userService;

    @GetMapping("/apiKey/exists")
    public ResponseEntity<Boolean> apiKeyExists(
            @RequestHeader("Api-Key-Hash") String apiKeyHash
    ){
        return ResponseEntity.ok(apiKeyService.exists(apiKeyHash));
    }

    @GetMapping("/users/{userId}/services")
    public ResponseEntity<List<String>>  getServiceNames(
        @PathVariable UUID userId
    ) {
        //TODO
    }

    @PostMapping("/account/register")
    public ResponseEntity<Void> register(
            @RequestBody @Valid RegisterRequest request
    ){
        userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/account/{id}/status")
    public ResponseEntity<Void> setIsActive(
            @PathVariable UUID id,
            @RequestBody @Valid StatusUpdateRequest request
    ) {
        userService.setIsActive(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/account/{id}")
    public ResponseEntity<Void> deleteAccountById(
            @PathVariable UUID id
    ) {
        userService.deleteAccount(id);
        return ResponseEntity.ok().build();
    }
}

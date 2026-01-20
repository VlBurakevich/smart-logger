package com.solution.coreservice.controller;

import com.solution.coreservice.dto.request.RegisterRequest;
import com.solution.coreservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
@RequestMapping("/api/internal")
public class InternalCoreController {
    private final AccountService accountService;

    @PostMapping("/accounts/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest registerRequest){
        accountService.register(registerRequest);
        return ResponseEntity.ok().build();
    }
}

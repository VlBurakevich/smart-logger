package com.solution.coreservice.service;

import com.solution.coreservice.dto.request.ApiKeyCreateRequest;
import com.solution.coreservice.dto.request.RegisterRequest;
import com.solution.coreservice.dto.response.ApiKeyResponse;
import com.solution.coreservice.entity.ApiKey;
import com.solution.coreservice.entity.User;
import com.solution.coreservice.exception.ServiceException;
import com.solution.coreservice.mapper.ApiKeyMapper;
import com.solution.coreservice.repository.ApiKeyRepository;
import com.solution.coreservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final UserRepository userRepository;
    private final ApiKeyRepository apiKeyRepository;
    private final ApiKeyMapper apiKeyMapper;

    @Transactional
    public void register(RegisterRequest registerRequest) {
        if (userRepository.existsById(registerRequest.getId())) {
            throw new ServiceException(HttpStatus.CONFLICT, "User ID already exists");
        }

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new ServiceException(HttpStatus.CONFLICT, "Username is already taken");
        }

        try {
            User user = new User();
            user.setUsername(registerRequest.getUsername());
            user.setId(registerRequest.getId());
            user.setIsActive(true);

            userRepository.save(user);
        } catch (Exception e) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "Core-service database error: " + e.getMessage());
        }
    }

    public List<ApiKeyResponse> getUserKeys(UUID userId) {
        return apiKeyRepository.findAllByUserId(userId)
                .stream()
                .map(apiKeyMapper::toResponse)
                .toList();
    }

    @Transactional
    public ApiKeyResponse createNewKey(ApiKeyCreateRequest apiKeyCreateRequest, UUID userId) {
        SecureRandom random = new SecureRandom();
        byte[] randomBytes = new byte[32];
        random.nextBytes(randomBytes);

        String rawApiKey = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

        String keyHash;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(rawApiKey.getBytes(StandardCharsets.UTF_8));
            keyHash = Base64.getUrlEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not supported", e);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "User not found"));

        ApiKey apiKey = ApiKey.builder()
                .user(user)
                .keyValue(keyHash)
                .name(apiKeyCreateRequest.getName())
                .description(apiKeyCreateRequest.getDescription())
                .build();

        return apiKeyMapper.toResponse(apiKeyRepository.save(apiKey));
    }

    @Transactional
    public void deleteKey(UUID userId, UUID keyId) {
        ApiKey key = apiKeyRepository.findById(keyId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "ApiKey not found"));

        if (!key.getUser().getId().equals(userId)) {
            throw new ServiceException(HttpStatus.FORBIDDEN, "You dont have permission to delete");
        }

        apiKeyRepository.delete(key);
    }
}

package com.solution.coreservice.service;

import com.solution.coreservice.dto.request.ApiKeyCreateRequest;
import com.solution.coreservice.dto.response.ApiKeyInfoResponse;
import com.solution.coreservice.dto.response.ApiKeyResponse;
import com.solution.coreservice.entity.ApiKey;
import com.solution.coreservice.entity.User;
import com.solution.coreservice.exception.ServiceException;
import com.solution.coreservice.mapper.ApiKeyMapper;
import com.solution.coreservice.repository.ApiKeyRepository;
import com.solution.coreservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApiKeyService {
    private final UserRepository userRepository;
    private final ApiKeyRepository apiKeyRepository;
    private final ApiKeyMapper apiKeyMapper;

    public Page<ApiKeyInfoResponse> getUserKeys(UUID userId, Pageable pageable) {
        return apiKeyRepository.findAllByUserId(userId, pageable)
                .map(apiKeyMapper::toInfoResponse);
    }

    @Transactional
    public ApiKeyResponse createNewKey(UUID userId, ApiKeyCreateRequest request) {
        SecureRandom random = new SecureRandom();
        byte[] randomBytes = new byte[32];
        random.nextBytes(randomBytes);

        String rawApiKey = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        String keyHash = DigestUtils.sha256Hex(rawApiKey);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "User not found"));

        ApiKey apiKey = ApiKey.builder()
                .user(user)
                .keyValueHash(keyHash)
                .name(request.getName())
                .description(request.getDescription())
                .build();

        apiKeyRepository.save(apiKey);

        return apiKeyMapper.toResponseWithKey(apiKey, rawApiKey);
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

    public boolean exists(String apiKeyHash) {
        return apiKeyRepository.existsApiKeyByKeyValueHash(apiKeyHash);
    }
}

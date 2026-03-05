package com.solution.coreservice.repository;

import com.solution.coreservice.entity.ApiKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ApiKeyRepository extends JpaRepository<ApiKey, UUID> {

    Page<ApiKey> findAllByUserId(UUID userId, Pageable pageable);

    boolean existsApiKeyByKeyValueHash(String keyValueHash);

    Optional<ApiKey> findByIdAndUserId(UUID id, UUID userId);
}

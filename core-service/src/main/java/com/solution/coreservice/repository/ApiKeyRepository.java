package com.solution.coreservice.repository;

import com.solution.coreservice.entity.ApiKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ApiKeyRepository extends JpaRepository<ApiKey, UUID> {

    List<ApiKey> findAllByUserId(UUID userId);

    boolean existsApiKeyByKeyValueHash(String keyValueHash);
}

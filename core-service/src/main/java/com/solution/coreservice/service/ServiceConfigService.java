package com.solution.coreservice.service;

import com.solution.coreservice.dto.request.ServiceConfigRequest;
import com.solution.coreservice.dto.response.ServiceConfigResponse;
import com.solution.coreservice.dto.response.ServiceConfigShortResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServiceConfigService {

    public ServiceConfigResponse get(UUID userId, String serviceConfigId) {
        return null;
    }

    public List<ServiceConfigShortResponse> getAll(UUID userId, Pageable pageable) {
        return null;
    }

    public ServiceConfigResponse update(UUID userId, ServiceConfigRequest serviceConfigRequest, String serviceConfigId) {
        return null;
    }

    public void delete(UUID userId, String serviceConfigId) {

    }
}

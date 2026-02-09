package com.solution.coreservice.service;

import com.solution.coreservice.dto.request.MonitoringTaskCreateRequest;
import com.solution.coreservice.dto.request.MonitoringTaskUpdateRequest;
import com.solution.coreservice.dto.response.MonitoringTaskResponse;
import com.solution.coreservice.dto.response.MonitoringTaskShortResponse;
import com.solution.coreservice.entity.ApiKey;
import com.solution.coreservice.entity.MonitoringTask;
import com.solution.coreservice.exception.ServiceException;
import com.solution.coreservice.mapper.MonitoringTaskMapper;
import com.solution.coreservice.repository.ApiKeyRepository;
import com.solution.coreservice.repository.MonitoringTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MonitoringTaskService {
    private final MonitoringTaskRepository monitoringTaskRepository;
    private final MonitoringTaskMapper monitoringTaskMapper;
    private final ApiKeyRepository apiKeyRepository;

    public MonitoringTaskResponse get(UUID userId, UUID monitoringTaskId) {
        return monitoringTaskRepository.findByIdAndApiKey_User_Id(monitoringTaskId, userId)
                .map(monitoringTaskMapper::toResponse)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Monitoring task not found"));
    }

    public Page<MonitoringTaskShortResponse> getAll(UUID userId, Pageable pageable) {
        Page<MonitoringTask> monitoringTasks = monitoringTaskRepository.findAllByApiKey_User_Id(userId, pageable);
        return monitoringTasks.map(monitoringTaskMapper::toShortResponse);
    }

    @Transactional
    public MonitoringTaskResponse create(MonitoringTaskCreateRequest request, UUID userId) {
        ApiKey apiKey = apiKeyRepository.findByIdAndUserId(request.getApiKeyId(), userId)
                .orElseThrow(() -> new ServiceException(HttpStatus.FORBIDDEN, "Access denied to Api Key"));

        MonitoringTask monitoringTask = monitoringTaskMapper.toEntity(request);
        monitoringTask.setApiKey(apiKey);

        return monitoringTaskMapper.toResponse(monitoringTaskRepository.save(monitoringTask));
    }

    @Transactional
    public MonitoringTaskResponse update(UUID userId, MonitoringTaskUpdateRequest request, UUID monitoringTaskId) {
        MonitoringTask monitoringTask = monitoringTaskRepository.findByIdAndApiKey_User_Id(monitoringTaskId, userId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Monitoring task not found"));

        monitoringTaskMapper.updateEntityFromDto(request, monitoringTask);

        if (request.getApiKeyId() != null && !monitoringTask.getApiKey().getId().equals(request.getApiKeyId())) {
            ApiKey newApiKey = apiKeyRepository.findByIdAndUserId(request.getApiKeyId(), userId)
                    .orElseThrow(() -> new ServiceException(HttpStatus.FORBIDDEN, "Access denied to new Api Key"));
            monitoringTask.setApiKey(newApiKey);
        }

        return monitoringTaskMapper.toResponse(monitoringTaskRepository.save(monitoringTask));
    }

    @Transactional
    public void delete(UUID userId, UUID monitoringTaskId) {
        if (!monitoringTaskRepository.existsByIdAndApiKey_User_Id(monitoringTaskId, userId)) {
            throw new ServiceException(HttpStatus.NOT_FOUND, "Monitoring task not found");
        }
        monitoringTaskRepository.deleteById(monitoringTaskId);
    }
}

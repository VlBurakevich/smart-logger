package com.solution.coreservice.mapper;


import com.solution.coreservice.dto.request.MonitoringTaskCreateRequest;
import com.solution.coreservice.dto.request.MonitoringTaskUpdateRequest;
import com.solution.coreservice.dto.response.MonitoringTaskResponse;
import com.solution.coreservice.dto.response.MonitoringTaskShortResponse;
import com.solution.coreservice.entity.ApiKey;
import com.solution.coreservice.entity.MonitoringTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface MonitoringTaskMapper {
    MonitoringTaskShortResponse toShortResponse(MonitoringTask monitoringTask);

    @Mapping(target = "apiKeyId", source = "apiKey.id")
    MonitoringTaskResponse toResponse(MonitoringTask monitoringTask);

    @Mapping(target = "apiKey", source = "apiKeyId", qualifiedByName = "idToApiKey")
    MonitoringTask toEntity(MonitoringTaskCreateRequest request);

    void updateEntityFromDto(MonitoringTaskUpdateRequest request, @MappingTarget MonitoringTask monitoringTask);

    @Named("idToApiKey")
    default ApiKey idToApiKey(UUID apiKeyId) {
        if (apiKeyId == null) return null;
        ApiKey apiKey = new ApiKey();
        apiKey.setId(apiKeyId);
        return apiKey;
    }
}

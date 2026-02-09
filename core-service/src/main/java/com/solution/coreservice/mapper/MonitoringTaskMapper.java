package com.solution.coreservice.mapper;


import com.solution.coreservice.dto.request.MonitoringTaskCreateRequest;
import com.solution.coreservice.dto.request.MonitoringTaskUpdateRequest;
import com.solution.coreservice.dto.response.MonitoringTaskResponse;
import com.solution.coreservice.dto.response.MonitoringTaskShortResponse;
import com.solution.coreservice.entity.MonitoringTask;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MonitoringTaskMapper {
    MonitoringTaskShortResponse toShortResponse(MonitoringTask monitoringTask);

    MonitoringTaskResponse toResponse(MonitoringTask monitoringTask);

    MonitoringTask toEntity(MonitoringTaskCreateRequest request);

    void updateEntityFromDto(MonitoringTaskUpdateRequest request, @MappingTarget MonitoringTask monitoringTask);
}

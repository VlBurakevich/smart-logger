package com.solution.coreservice.mapper;

import com.solution.coreservice.dto.messaging.InferenceSnapshotResponse;
import com.solution.coreservice.dto.messaging.SnapshotAlert;
import com.solution.coreservice.dto.response.SnapshotResponse;
import com.solution.coreservice.dto.response.SnapshotShortResponse;
import com.solution.coreservice.entity.Snapshot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SnapshotMapper {

    SnapshotResponse toResponse(Snapshot snapshot);

    SnapshotShortResponse toShortResponse(Snapshot snapshot);

    void updateSnapshot(@MappingTarget Snapshot snapshot, InferenceSnapshotResponse response);

    @Mapping(target = "snapshotId", source = "id")
    SnapshotAlert toAlert(Snapshot snapshot, String serviceName, UUID userId);
}

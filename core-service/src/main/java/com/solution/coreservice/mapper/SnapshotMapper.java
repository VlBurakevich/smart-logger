package com.solution.coreservice.mapper;

import com.solution.coreservice.dto.messaging.InferenceSnapshotResponse;
import com.solution.coreservice.dto.response.SnapshotResponse;
import com.solution.coreservice.dto.response.SnapshotShortResponse;
import com.solution.coreservice.entity.Snapshot;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SnapshotMapper {

    SnapshotResponse toResponse(Snapshot snapshot);

    SnapshotShortResponse toShortResponse(Snapshot snapshot);

    void updateSnapshot(@MappingTarget Snapshot snapshot, InferenceSnapshotResponse response);
}

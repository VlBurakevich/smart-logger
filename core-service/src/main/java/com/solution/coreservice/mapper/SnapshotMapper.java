package com.solution.coreservice.mapper;

import com.solution.coreservice.dto.response.SnapshotResponse;
import com.solution.coreservice.dto.response.SnapshotShortResponse;
import com.solution.coreservice.entity.Snapshot;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SnapshotMapper {

    SnapshotResponse toResponse(Snapshot snapshot);

    SnapshotShortResponse toShortResponse(Snapshot snapshot);
}

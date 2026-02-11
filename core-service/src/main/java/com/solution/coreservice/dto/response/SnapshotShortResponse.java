package com.solution.coreservice.dto.response;

import com.solution.coreservice.entity.SnapshotStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SnapshotShortResponse {
    private UUID id;
    private SnapshotStatus snapshotStatus;
    private String aiDescription;
}

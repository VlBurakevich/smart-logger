package com.solution.coreservice.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.solution.coreservice.entity.SnapshotStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SnapshotResponse {
    private UUID id;
    private OffsetDateTime snapshotTime;
    private JsonNode errors;
    private Double maxSeverity;
    private String aiDescription;
    private Double aiScore;
    private OffsetDateTime createdAt;
}

package com.solution.coreservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitoringTaskResponse {
    private UUID id;
    private UUID apiKeyId;
    private String serviceName;
    private Integer snapshotIntervalSec;
    private Integer reportIntervalHr;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}

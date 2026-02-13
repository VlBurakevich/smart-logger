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
    private Integer checkSec;
    private Integer reportHr;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}

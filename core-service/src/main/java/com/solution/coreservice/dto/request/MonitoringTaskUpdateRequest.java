package com.solution.coreservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
//TODO Create annotation @AtLeastOneNotNull
public class MonitoringTaskUpdateRequest {

    private UUID apiKeyId;

    private String serviceName;

    private Integer checkMin;

    private Integer reportHr;
}

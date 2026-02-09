package com.solution.coreservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitoringTaskShortResponse {
    private UUID id;
    private String serviceName;
    private Integer checkMin;
    private Integer reportHr;
}

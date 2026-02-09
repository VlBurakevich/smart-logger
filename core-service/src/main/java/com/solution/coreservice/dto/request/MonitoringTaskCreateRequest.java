package com.solution.coreservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitoringTaskCreateRequest {

    @NotNull
    private UUID apiKeyId;

    @NotBlank
    private String serviceName;

    @NotNull
    private Integer checkMin;

    @NotNull
    private Integer reportHr;
}

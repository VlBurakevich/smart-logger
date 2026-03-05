package com.solution.coreservice.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {
    private UUID id;
    private JsonNode content;
    private Integer errorCount;
    private Integer anomalyCount;
    private String summary;
    private OffsetDateTime createdAt;
}

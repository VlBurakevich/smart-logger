package com.solution.coreservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportShortResponse {
    private UUID id;
    private Integer errorCount;
    private Integer anomalyCount;
    private String summaryPreview;
    private OffsetDateTime createdAt;
}

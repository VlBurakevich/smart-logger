package com.solution.modelinferenceservice.dto;

import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record InferenceSnapshotResult(
    String taskId,
    JsonNode errors,
    String aiDescription,
    BigDecimal aiScore,
    OffsetDateTime createdAt
) {
}

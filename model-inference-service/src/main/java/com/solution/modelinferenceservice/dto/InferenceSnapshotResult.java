package com.solution.modelinferenceservice.dto;

import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record InferenceSnapshotResult(
    UUID snapshotId,
    JsonNode errors,
    String aiDescription,
    String rootCase,
    String suggestedAction,
    BigDecimal aiScore,
    OffsetDateTime createdAt
) {}

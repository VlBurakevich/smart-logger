package com.solution.coreservice.dto.messaging;

import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record InferenceSnapshotResponse (
        UUID snapshotId,
        JsonNode errors,
        String rootCase,
        String suggestedAction,
        String aiDescription,
        BigDecimal aiScore,
        OffsetDateTime createdAt
) {}
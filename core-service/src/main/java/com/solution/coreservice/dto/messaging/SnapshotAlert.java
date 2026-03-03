package com.solution.coreservice.dto.messaging;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record SnapshotAlert(
   UUID snapshotId,
   UUID userId,
   String serviceName,
   BigDecimal aiScore,
   String rootCause,
   String aiDescription,
   OffsetDateTime createdAt
) {}

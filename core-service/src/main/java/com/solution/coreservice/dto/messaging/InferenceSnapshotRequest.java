package com.solution.coreservice.dto.messaging;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record InferenceSnapshotRequest(
        UUID snapshotId,
        OffsetDateTime sendAt,
        List<LogEntry> logs
) {}
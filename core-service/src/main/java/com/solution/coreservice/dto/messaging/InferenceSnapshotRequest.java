package com.solution.coreservice.dto.messaging;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record InferenceSnapshotRequest(
        @JsonProperty("task_id") UUID taskId,
        @JsonProperty("send_at") OffsetDateTime sendAt,
        @JsonProperty("logs") List<LogEntry> logs
) {}

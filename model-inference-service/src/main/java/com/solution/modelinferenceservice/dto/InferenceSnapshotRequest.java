package com.solution.modelinferenceservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record InferenceSnapshotRequest(
        @JsonProperty("task_id") UUID taskId,
        @JsonProperty("snapshot_id") UUID snapshotId,
        @JsonProperty("service_name") String serviceName,
        @JsonProperty("api_key_hash") String apiKeyHash,
        @JsonProperty("send_at") OffsetDateTime sendAt,
        @JsonProperty("logs") List<LogEntry> logs
){}

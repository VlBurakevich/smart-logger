package com.solution.coreservice.dto.messaging;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record InferenceRequest(
        @JsonProperty("task_id") UUID taskId,
        @JsonProperty("service_name") String serviceName,
        @JsonProperty("api_key_hash") String apiKeyHash,
        @JsonProperty("send_at") OffsetDateTime sendAt,
        @JsonProperty("logs") List<LogResponse> logs
) {}

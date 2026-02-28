package com.solution.coreservice.dto.messaging;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record LogEntry(
        @JsonProperty("_msg")
        String message,
        String level,
        @JsonProperty("logger")
        String loggerName,
        @JsonProperty("_time")
        OffsetDateTime timestamp
) {
}

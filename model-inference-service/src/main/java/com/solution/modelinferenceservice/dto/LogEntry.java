package com.solution.modelinferenceservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public record LogEntry(
        String level,
        @JsonProperty("_msg")
        String message,
        @JsonProperty("logger")
        String loggerName,
        @JsonProperty("_time")
        OffsetDateTime timestamp
) {
}

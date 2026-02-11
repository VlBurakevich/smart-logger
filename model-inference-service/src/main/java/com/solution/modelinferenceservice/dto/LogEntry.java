package com.solution.modelinferenceservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LogEntry(
        @JsonProperty("timestamp")
        String timestamp,
        @JsonProperty("level")
        String level,
        @JsonProperty("message")
        String message
) { }

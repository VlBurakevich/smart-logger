package com.solution.modelinferenceservice.dto;

import java.time.OffsetDateTime;

public record LogEntry(
        String level,
        String message,
        String loggerName,
        OffsetDateTime timestamp
) {
}

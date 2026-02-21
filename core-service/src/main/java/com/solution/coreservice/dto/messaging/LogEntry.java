package com.solution.coreservice.dto.messaging;

import java.time.OffsetDateTime;

public record LogEntry(
        String level,
        String message,
        String loggerName,
        OffsetDateTime timestamp
) {
}

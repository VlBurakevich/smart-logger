package com.solution.notificationservice.dto;

import java.util.Map;
import java.util.UUID;

public record ReportResult(
        UUID userId,
        String summary,
        Map<String, Integer> logLevelCount,
        String period
) {
}

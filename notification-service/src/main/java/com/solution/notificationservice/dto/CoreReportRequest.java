package com.solution.notificationservice.dto;

import java.util.List;
import java.util.UUID;

public record CoreReportRequest(
        UUID userId,
        List<String> serviceNames,
        Integer periodHours
) {
}

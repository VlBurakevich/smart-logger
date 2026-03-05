package com.solution.notificationservice.dto;

import java.util.List;

public record ReportArgs(
        Integer periodHours,
        List<String> serviceNames
) {
}

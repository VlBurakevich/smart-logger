package com.solution.coreservice.dto.messaging;

import java.util.List;
import java.util.UUID;

public record ReportRequest(
        UUID userId,
        List<String> serviceNames,
        Integer periodHours
) {}

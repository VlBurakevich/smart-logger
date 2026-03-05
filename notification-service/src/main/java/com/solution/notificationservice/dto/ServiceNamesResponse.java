package com.solution.notificationservice.dto;

import java.util.List;

public record ServiceNamesResponse(
        List<String> serviceNames
) {
}

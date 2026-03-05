package com.solution.coreservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiKeyInfoResponse {
    private String id;
    private String name;
    private String description;
    private OffsetDateTime createdAt;
}

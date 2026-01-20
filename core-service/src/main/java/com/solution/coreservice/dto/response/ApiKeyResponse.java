package com.solution.coreservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiKeyResponse {
    private String id;
    private String apiKey;
    private String name;
    private String description;
    private OffsetDateTime createdAt;
}

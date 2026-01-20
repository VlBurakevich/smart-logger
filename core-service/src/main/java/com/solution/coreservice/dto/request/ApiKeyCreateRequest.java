package com.solution.coreservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiKeyCreateRequest {

    @NotBlank(message = "name cannot be empty")
    private String name;

    private String description;
}

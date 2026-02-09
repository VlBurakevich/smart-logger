package com.solution.logingestionservice.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogEventRequest {
    @NotBlank
    private String level;
    @NotBlank
    @Size(max = 65535)
    private String message;
    @NotBlank
    private String loggerName;
    @NotEmpty
    private OffsetDateTime timestamp;
}

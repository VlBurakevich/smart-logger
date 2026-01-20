package com.solution.coreservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LogEventRequest {
    @NotBlank
    private String accountId;
    @NotBlank
    private String level;
    @NotBlank
    @Size(max = 65535)
    private String message;
    @NotBlank
    private String logServiceName;
    @NotEmpty
    private long timestamp;
}

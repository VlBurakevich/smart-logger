package com.solution.logingestionservice.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogEventRequest {
    @NotBlank
    private String apiKey; //move to header
    @NotBlank
    private String level;
    @NotBlank
    @Size(max = 65535)
    private String message;
    @NotBlank
    private String serviceName;
    @NotEmpty
    private long timestamp;
}

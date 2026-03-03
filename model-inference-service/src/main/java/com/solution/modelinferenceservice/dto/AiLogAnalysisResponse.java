package com.solution.modelinferenceservice.dto;

import com.fasterxml.jackson.databind.JsonNode;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

public record AiLogAnalysisResponse(
        JsonNode errors,
        String aiDescription,
        String rootCause,
        String suggestedAction,
        @Min(0) @Max(1) BigDecimal aiScore
) {
}

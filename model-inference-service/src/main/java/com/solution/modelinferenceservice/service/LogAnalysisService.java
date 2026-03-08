package com.solution.modelinferenceservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.solution.modelinferenceservice.dto.AiLogAnalysisResponse;
import com.solution.modelinferenceservice.dto.InferenceSnapshotRequest;
import com.solution.modelinferenceservice.dto.InferenceSnapshotResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Slf4j
@Service
public class LogAnalysisService {
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    public LogAnalysisService(ChatClient.Builder chatClientBuilder, ObjectMapper objectMapper) {
        this.chatClient = chatClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    @Value("classpath:/prompts/analyze-snapshot-log.st")
    private Resource analyzeLogsPromptResource;


    @Retryable(
            retryFor = {RuntimeException.class, JsonProcessingException.class},
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public InferenceSnapshotResult analyzeLogs(InferenceSnapshotRequest request) throws  JsonProcessingException {
        String rawAnswer = chatClient.prompt()
                .user(u -> u.text(analyzeLogsPromptResource)
                        .param("taskId", request.snapshotId())
                        .param("logs", request.logs()))
                .call()
                .content();

        String cleanJson = sanitize(rawAnswer);

        try {
            AiLogAnalysisResponse aiResponse = objectMapper.readValue(cleanJson, AiLogAnalysisResponse.class);

            JsonNode errorsNode = (aiResponse.errors() != null)
                    ? objectMapper.valueToTree(aiResponse.errors())
                    : JsonNodeFactory.instance.arrayNode();

            return new InferenceSnapshotResult(
                    request.snapshotId(),
                    errorsNode,
                    aiResponse.aiDescription(),
                    aiResponse.rootCause(),
                    aiResponse.suggestedAction(),
                    aiResponse.aiScore(),
                    OffsetDateTime.now(ZoneOffset.UTC)
            );
        } catch (JsonProcessingException e) {
            log.error("Final JSON is still invalid after sanitization {}", e.getMessage());
            throw e;
        }
    }

    private String sanitize(String input) {
        if (input == null) return "{}";
        String result = input.trim();
        if (result.contains("```")) {
            result = result.replaceAll("(?s)```(?:json)?\\s*(.*?)\\s*```", "$1").trim();
        }
        return result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1);
    }
}

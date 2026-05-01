package com.solution.modelinferenceservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class LogAnalysisService {
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    public LogAnalysisService(ChatClient.Builder chatClientBuilder, ObjectMapper objectMapper) {
        this.chatClient = chatClientBuilder.build();
        this.objectMapper = objectMapper;
    }

    @Value("classpath:/prompts/analyze-log.st")
    private Resource analyzeLogsPromptResource;


    @Retryable(
            retryFor = {RuntimeException.class, JsonProcessingException.class},
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    public InferenceSnapshotResult analyzeLogs(InferenceSnapshotRequest request) throws  JsonProcessingException {

        String logsJson = objectMapper.writeValueAsString(request.logs());
        String prompt = renderPrompt(request.snapshotId().toString(), logsJson);

        String rawAnswer = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        String cleanJson = sanitize(rawAnswer);
        String normalizedJson = normalizeAiResponse(cleanJson);

        try {
            AiLogAnalysisResponse aiResponse = objectMapper.readValue(normalizedJson, AiLogAnalysisResponse.class);

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

    private String renderPrompt(String taskId, String logs) {
        try {
            String template = analyzeLogsPromptResource.getContentAsString(StandardCharsets.UTF_8);

            return template
                    .replace("$taskId$", taskId)
                    .replace("$logs$", logs);
        } catch (IOException e) {
            log.error("Failed to read prompt template", e);
            throw new RuntimeException("Failed to read prompt template", e);
        }
    }

    private String sanitize(String input) {
        if (input == null) return "{}";
        String result = input.trim();
        if (result.contains("```")) {
            result = result.replaceAll("(?s)```(?:json)?\\s*(.*?)\\s*```", "$1").trim();
        }

        int start = result.indexOf("{");
        int end = result.lastIndexOf("}");
        if (start == -1 || end == -1 || end <= start) {
            throw new RuntimeException("AI response does not contain valid JSON object");
        }

        return result.substring(start, end + 1);
    }

    private String normalizeAiResponse(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);

            if (root.has("suggestedAction")) {
                JsonNode action = root.get("suggestedAction");
                if (action.isArray()) {
                    String joined = StreamSupport.stream(action.spliterator(), false)
                            .map(JsonNode::asText)
                            .collect(Collectors.joining("; "));
                    ((ObjectNode) root).put("suggestedAction", joined);
                    log.debug("Normalized suggestedAction: array -> string");
                }
            }

            if (root.has("aiScore")) {
                JsonNode score = root.get("aiScore");
                if (score.isTextual()) {
                    ((ObjectNode) root).put("aiScore", score.asDouble());
                }
            }

            if (!root.has("errors") || root.get("errors").isNull()) {
                ((ObjectNode) root).set("errors", JsonNodeFactory.instance.arrayNode());
            }

            return objectMapper.writeValueAsString(root);
        } catch (JsonProcessingException e) {
            log.warn("Failed to normalize AI response, using original. Error: {}", e.getMessage());
            return json;
        }
    }
}

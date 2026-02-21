package com.solution.modelinferenceservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.solution.modelinferenceservice.dto.AiLogAnalysisResponse;
import com.solution.modelinferenceservice.dto.InferenceSnapshotRequest;
import com.solution.modelinferenceservice.dto.InferenceSnapshotResult;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
public class LogAnalysisService {
    private final ChatClient chatClient;

    public LogAnalysisService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Value("classpath:/prompts/analyze-log.st")
    private Resource analyzeLogsPromptResource;


    @Retryable(
            retryFor = {Exception.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public InferenceSnapshotResult analyzeLogs(InferenceSnapshotRequest request) {
        AiLogAnalysisResponse aiResponse = chatClient.prompt()
                .user(u -> u.text(analyzeLogsPromptResource)
                        .param("taskId", request.snapshotId())
                        .param("logs", request.logs()))
                .call()
                .entity(AiLogAnalysisResponse.class);

        JsonNode errorsNode = (aiResponse.errors() != null)
                ? aiResponse.errors()
                : JsonNodeFactory.instance.arrayNode();

        return new InferenceSnapshotResult(
                request.snapshotId(),
                errorsNode,
                aiResponse.aiDescription(),
                aiResponse.rootCase(),
                aiResponse.suggestedAction(),
                aiResponse.aiScore(),
                OffsetDateTime.now(ZoneOffset.UTC)
        );
    }
}

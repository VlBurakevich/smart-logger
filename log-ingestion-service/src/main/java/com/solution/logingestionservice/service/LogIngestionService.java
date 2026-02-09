package com.solution.logingestionservice.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.solution.logingestionservice.dto.LogEventRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogIngestionService {
    private final ObjectMapper objectMapper;
    private final RestClient victoriaRestClient;

    public void ingest(List<LogEventRequest> logEvents, String apiKeyHash, String serviceName) {
        if (logEvents == null || logEvents.isEmpty()) return;

        String body = logEvents.stream()
                .map(event -> createJsonLine(event, apiKeyHash, serviceName))
                .collect(Collectors.joining("\n", "", "\n"));


        CompletableFuture.runAsync(() -> {
            try {
                victoriaRestClient.post()
                        .uri(uriBuilder -> uriBuilder
                                .queryParam("_stream_fields", "api_key,service")
                                .queryParam("_time_field", "timestamp")
                                .build())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Api-Key-Hash", apiKeyHash)
                        .body(body)
                        .retrieve()
                        .toBodilessEntity();
            } catch (Exception e) {
                log.error("Failed to push batch to Victoria Log Ingestion Service: {}", e.getMessage());
            }
        });
    }

    private String createJsonLine(LogEventRequest event, String apiKeyHash, String serviceName) {
        try {
            Map<String, Object> line = new HashMap<>();
            line.put("level", event.getLevel());
            line.put("message", event.getMessage());
            line.put("logger", event.getLoggerName());
            line.put("timestamp", event.getTimestamp());

            line.put("api_key_hash", apiKeyHash);
            line.put("service", serviceName);

            return objectMapper.writeValueAsString(line);
        } catch (Exception e) {
            return "{}";
        }
    }
}


package com.solution.coreservice.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solution.coreservice.dto.messaging.LogEntry;
import com.solution.coreservice.entity.MonitoringTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class VictoriaLogsClient {
    private final RestClient victoriaRestClient;
    private final ObjectMapper objectMapper;

    public List<LogEntry> fetchLogs(MonitoringTask task, int limit) {
        OffsetDateTime fromTime = task.getLastSnapshotAt() != null
                ? task.getLastSnapshotAt()
                : OffsetDateTime.now(ZoneOffset.UTC);

        String fromParam = fromTime.atZoneSameInstant(ZoneOffset.UTC)
                .format(DateTimeFormatter.ISO_INSTANT);

        String query = String.format("_stream:%%7Bapi_key_hash=\"%s\", service=\"%s\"%%7D",
                task.getApiKey().getKeyValueHash(), task.getServiceName());

        return victoriaRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .replacePath("/select/logsql/query")
                        .queryParam("query", query)
                        .queryParam("limit", limit)
                        .queryParam("from", fromParam)
                        .build())
                .exchange((request, response) -> {
                    if (response.getStatusCode().isError()) {
                        log.error("VictoriaLogs API error: {} {}", response.getStatusCode(), response.getStatusText());
                        return List.of();
                    }
                    try (InputStream is = response.getBody();
                         BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

                        return reader.lines()
                                .filter(line -> !line.isBlank())
                                .map(this::parseLine)
                                .filter(Objects::nonNull)
                                .limit(limit)
                                .toList();
                    } catch (IOException e) {
                        log.error("Failed to process VictoriaLogs response stream", e);
                        return List.of();
                    }
                });
    }

    private LogEntry parseLine(String line) {
        try {
            return objectMapper.readValue(line, LogEntry.class);
        } catch (Exception e) {
            log.error("Failed to parse log line: {}", line, e);
            return null;
        }
    }
}

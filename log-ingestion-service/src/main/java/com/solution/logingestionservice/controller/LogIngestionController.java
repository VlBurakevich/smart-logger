package com.solution.logingestionservice.controller;


import com.solution.logingestionservice.dto.LogEventRequest;
import com.solution.logingestionservice.service.LogIngestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ingestion")
@RequiredArgsConstructor
public class LogIngestionController {
    private final LogIngestionService ingestionService;

    @PostMapping("/batch")
    public ResponseEntity<Void> ingestBatchLog(
            @Valid @RequestBody List<LogEventRequest> logEvents,
            @RequestHeader("X-Api-Key") String apiKey
    ) {
        ingestionService.ingest(logEvents, apiKey);
        return ResponseEntity.accepted().build();
    }
}

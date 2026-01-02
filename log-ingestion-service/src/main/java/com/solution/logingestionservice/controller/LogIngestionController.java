package com.solution.logingestionservice.controller;


import com.solution.logingestionservice.dto.LogEventDto;
import com.solution.logingestionservice.service.LogIngestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class LogIngestionController {
    private final LogIngestionService ingestionService;

    @PostMapping("/batch")
    public ResponseEntity<Void> ingestLog(@Valid @RequestBody List<LogEventDto> logEvent) {
        ingestionService.sendBatchToKafka(logEvent);
        return ResponseEntity.accepted().build();
    }
}

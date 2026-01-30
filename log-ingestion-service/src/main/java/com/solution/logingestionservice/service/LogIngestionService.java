package com.solution.logingestionservice.service;


import com.solution.logingestionservice.dto.LogEventRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogIngestionService {
    public void ingest(@Valid List<LogEventRequest> logEvents, String apiKey) {

    }
}

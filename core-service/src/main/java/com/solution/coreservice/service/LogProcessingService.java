package com.solution.coreservice.service;

import com.solution.coreservice.client.VictoriaLogsClient;
import com.solution.coreservice.dto.messaging.InferenceRequest;
import com.solution.coreservice.dto.messaging.LogResponse;
import com.solution.coreservice.entity.MonitoringTask;
import com.solution.coreservice.messaging.inference.InferenceProducer;
import com.solution.coreservice.repository.MonitoringTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogProcessingService {
    @Value("${victorialogs.batch.limit:100}")
    private int limit;

    private final VictoriaLogsClient victoriaLogsClient;
    private final MonitoringTaskRepository monitoringTaskRepository;
    private final InferenceProducer inferenceProducer;

    public void processSnapshot(MonitoringTask task) {
        List<LogResponse> logs = victoriaLogsClient.fetchLogs(task, limit);
        if (logs.isEmpty()) {
            task.setLastCheckedAt(OffsetDateTime.now(ZoneOffset.UTC));
            monitoringTaskRepository.save(task);
            return;
        }

        InferenceRequest request = new InferenceRequest(
                task.getId(),
                task.getServiceName(),
                task.getApiKey().getKeyValueHash(),
                OffsetDateTime.now(ZoneOffset.UTC),
                logs
        );

        inferenceProducer.sendToSnapshot(request);
    }

    public void processReport(MonitoringTask task) {

    }
}

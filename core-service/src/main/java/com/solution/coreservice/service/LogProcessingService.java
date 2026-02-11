package com.solution.coreservice.service;

import com.solution.coreservice.client.VictoriaLogsClient;
import com.solution.coreservice.dto.messaging.InferenceSnapshotRequest;
import com.solution.coreservice.dto.messaging.LogEntry;
import com.solution.coreservice.entity.MonitoringTask;
import com.solution.coreservice.messaging.inference.InferenceSnapshotProducer;
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
    private final InferenceSnapshotProducer inferenceSnapshotProducer;

    public void processSnapshot(MonitoringTask task) {
        List<LogEntry> logs = victoriaLogsClient.fetchLogs(task, limit);
        if (logs.isEmpty()) {
            task.setLastCheckedAt(OffsetDateTime.now(ZoneOffset.UTC));
            monitoringTaskRepository.save(task);
            return;
        }

        InferenceSnapshotRequest request = new InferenceSnapshotRequest(
                task.getId(),
                task.getServiceName(),
                task.getApiKey().getKeyValueHash(),
                OffsetDateTime.now(ZoneOffset.UTC),
                logs
        );

        inferenceSnapshotProducer.sendToSnapshot(request);
    }

    public void processReport(MonitoringTask task) {

    }
}

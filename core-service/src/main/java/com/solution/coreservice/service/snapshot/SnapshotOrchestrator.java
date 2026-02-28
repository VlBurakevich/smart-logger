package com.solution.coreservice.service.snapshot;

import com.solution.coreservice.client.VictoriaLogsClient;
import com.solution.coreservice.dto.messaging.InferenceSnapshotRequest;
import com.solution.coreservice.dto.messaging.LogEntry;
import com.solution.coreservice.entity.MonitoringTask;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SnapshotOrchestrator {

    @Value("${victorialogs.batch.limit:100}")
    private int limit;

    private final SnapshotPersistenceService snapshotPersistenceService;
    private final VictoriaLogsClient victoriaLogsClient;
    private final LogPreProcessor logPreProcessor;

    public void processSnapshot(MonitoringTask task) {
        List<LogEntry> logs = victoriaLogsClient.fetchLogs(task, limit);

        if (logPreProcessor.isSimpleEnoughFromLocalAnalyze(logs)) {
            String summary = logPreProcessor.performLocalAnalyze(task, logs);
            snapshotPersistenceService.completeLocally(task, summary);
        } else {
            InferenceSnapshotRequest request = new InferenceSnapshotRequest(
                    task.getCurrentSnapshot().getId(),
                    OffsetDateTime.now(ZoneOffset.UTC),
                    logs
            );

            snapshotPersistenceService.sendToInference(request, task);
        }
    }
}

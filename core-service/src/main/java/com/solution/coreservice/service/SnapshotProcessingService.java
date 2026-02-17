package com.solution.coreservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solution.coreservice.client.VictoriaLogsClient;
import com.solution.coreservice.dto.messaging.InferenceSnapshotRequest;
import com.solution.coreservice.dto.messaging.LogEntry;
import com.solution.coreservice.entity.MonitoringTask;
import com.solution.coreservice.entity.OutboxMessage;
import com.solution.coreservice.entity.OutboxStatus;
import com.solution.coreservice.entity.Snapshot;
import com.solution.coreservice.entity.SnapshotStatus;
import com.solution.coreservice.exception.ServiceException;
import com.solution.coreservice.repository.MonitoringTaskRepository;
import com.solution.coreservice.repository.OutboxRepository;
import com.solution.coreservice.repository.SnapshotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SnapshotProcessingService {
    @Value("${victorialogs.batch.limit:100}")
    private int limit;

    @Value("${app.kafka.inference-request}")
    private String snapshotRequestTopic;

    private final OutboxRepository outboxRepository;
    private final VictoriaLogsClient victoriaLogsClient;
    private final MonitoringTaskRepository monitoringTaskRepository;
    private final SnapshotRepository snapshotRepository;
    private final ObjectMapper objectMapper;
    private final ObjectProvider<SnapshotProcessingService> self;

    public void processSnapshot(MonitoringTask task) {
        List<LogEntry> logs = victoriaLogsClient.fetchLogs(task, limit);
        InferenceSnapshotRequest request = new InferenceSnapshotRequest(
                task.getCurrentSnapshot().getId(),
                OffsetDateTime.now(ZoneOffset.UTC),
                logs
        );

        self.ifAvailable(service -> service.sendToInference(request, task));
    }

    @Transactional
    public void sendToInference(InferenceSnapshotRequest request, MonitoringTask task) {
        Snapshot snapshot = task.getCurrentSnapshot();

        if (snapshot == null) {
            throw new IllegalArgumentException("snapshot is null");
        }
        snapshot.setStatus(SnapshotStatus.AWAITING_REPLY);

        OutboxMessage outboxMessage = new OutboxMessage();
        outboxMessage.setTopic(snapshotRequestTopic);

        try {
            outboxMessage.setPayload(objectMapper.valueToTree(request));
        } catch (Exception e) {
            throw new ServiceException(HttpStatus.NOT_FOUND, e.getMessage());
        }

        outboxMessage.setStatus(OutboxStatus.PENDING);
        outboxMessage.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));

        outboxRepository.save(outboxMessage);
    }

    public void finalizeSnapshot() {
        //TODO
    }

    @Transactional
    public List<MonitoringTask> captureTasks(int batchSize) {
        List<MonitoringTask> tasks = monitoringTaskRepository.findReadyForSnapshot(batchSize);

        if (tasks.isEmpty()) {
            return List.of();
        }

        List<Snapshot> preparedSnapshots = new ArrayList<>();
        OffsetDateTime now  = OffsetDateTime.now(ZoneOffset.UTC);

        for (MonitoringTask task : tasks) {
            Snapshot snapshot = new Snapshot();
            snapshot.setMonitoringTask(task);
            snapshot.setStatus(SnapshotStatus.PENDING);

            preparedSnapshots.add(snapshot);

            task.setCurrentSnapshot(snapshot);
            task.setLastSnapshotAt(now);
        }

        snapshotRepository.saveAll(preparedSnapshots);

        return tasks;
    }

    @Transactional
    public void handleFailure(MonitoringTask task) {
        monitoringTaskRepository.findById(task.getId()).ifPresent(actualTask -> {
            Snapshot snapshot = actualTask.getCurrentSnapshot();

            if (snapshot != null) {
                snapshot.setStatus(SnapshotStatus.FAILED);
            }
            actualTask.setCurrentSnapshot(null);
        });
    }
}

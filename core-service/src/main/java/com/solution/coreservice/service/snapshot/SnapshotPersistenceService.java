package com.solution.coreservice.service.snapshot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.solution.coreservice.dto.messaging.InferenceSnapshotRequest;
import com.solution.coreservice.dto.messaging.InferenceSnapshotResponse;
import com.solution.coreservice.entity.MonitoringTask;
import com.solution.coreservice.entity.Snapshot;
import com.solution.coreservice.entity.SnapshotStatus;
import com.solution.coreservice.mapper.SnapshotMapper;
import com.solution.coreservice.repository.ApiKeyRepository;
import com.solution.coreservice.repository.MonitoringTaskRepository;
import com.solution.coreservice.repository.SnapshotRepository;
import com.solution.coreservice.service.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SnapshotPersistenceService {

    @Value("${app.kafka.inference-request}")
    private String snapshotRequestTopic;

    @Value("${app.kafka.notification-snapshot-alert}")
    private String snapshotAlertTopic;

    private final OutboxService outboxService;
    private final MonitoringTaskRepository monitoringTaskRepository;
    private final ApiKeyRepository apiKeyRepository;
    private final SnapshotRepository snapshotRepository;
    private final SnapshotMapper snapshotMapper;
    private final ObjectMapper objectMapper;

    @Transactional
    public void sendToInference(InferenceSnapshotRequest request, MonitoringTask task) {
        Snapshot snapshot = task.getCurrentSnapshot();

        if (snapshot == null) {
            throw new IllegalArgumentException("snapshot is null");
        }
        snapshot.setStatus(SnapshotStatus.AWAITING_REPLY);
        snapshotRepository.save(snapshot);

        outboxService.saveEvent(snapshotRequestTopic, request);
    }

    @Transactional
    public List<MonitoringTask> captureTasks(int batchSize) {
        List<MonitoringTask> tasks = monitoringTaskRepository.findReadyForSnapshot(batchSize);

        log.info(">>_>> find tasks {}", tasks.toString());

        if (tasks.isEmpty()) {
            return List.of();
        }

        preloadApiKeys(tasks);

        List<Snapshot> preparedSnapshots = new ArrayList<>();
        List<MonitoringTask> capturedTasks = new ArrayList<>();

        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

        for (MonitoringTask task : tasks) {
            createSnapshotIfReady(task, now).ifPresent(snapshot -> {
                preparedSnapshots.add(snapshot);
                capturedTasks.add(task);
                task.setCurrentSnapshot(snapshot);
                task.setLastSnapshotAt(snapshot.getSnapshotEndTime());
            });
        }

        snapshotRepository.saveAll(preparedSnapshots);

        return capturedTasks;
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

    @Transactional
    public void complete(InferenceSnapshotResponse response) {
        Snapshot snapshot = findSnapshot(response.snapshotId());
        if (snapshot == null) {
            return;
        }
        snapshotMapper.updateSnapshot(snapshot, response);
        snapshot.setStatus(SnapshotStatus.COMPLETED);

        MonitoringTask task = snapshot.getMonitoringTask();
        task.setCurrentSnapshot(null);
        monitoringTaskRepository.save(task);
        if (snapshot.getAiScore() >= 0.6) { //TODO optimize
            outboxService.saveEvent(
                    snapshotAlertTopic,
                    snapshotMapper.toAlert(
                            snapshot,
                            task.getServiceName(),
                            task.getApiKey().getUser().getId()
                    )
            );
        }
    }

    @Transactional
    public void completeLocally(MonitoringTask task, String localSummary) {
        Snapshot snapshot = findSnapshot(task.getCurrentSnapshot().getId());
        if (snapshot == null) {
            return;
        }

        ObjectNode errorsNode = (snapshot.getErrors() == null)
                ? objectMapper.createObjectNode()
                : (ObjectNode) snapshot.getErrors();

        errorsNode.put("localSummary", localSummary);

        snapshot.setErrors(errorsNode);
        snapshot.setStatus(SnapshotStatus.COMPLETED);

        task.setCurrentSnapshot(null);
        monitoringTaskRepository.save(task);
    }

    private Snapshot findSnapshot(UUID snapshotId) {
        Optional<Snapshot> snapshotOpt = snapshotRepository.findById(snapshotId);

        if (snapshotOpt.isEmpty()) {
            log.error("CRITICAL: Received inference result for non-existent snapshot ID: {}", snapshotId);
            return null;
        }

        return snapshotOpt.get();
    }

    private Optional<Snapshot> createSnapshotIfReady(MonitoringTask task, OffsetDateTime now) {
        OffsetDateTime startTime = (task.getLastSnapshotAt() != null)
                ? task.getLastSnapshotAt()
                : task.getCreatedAt();

        OffsetDateTime endTime = startTime.plusSeconds(task.getSnapshotIntervalSec());

        if (endTime.plusSeconds(5).isAfter(now)) {
            log.debug("Task {} is not ready yet. Next window ends at {}", task.getId(), endTime);
            return Optional.empty();
        }

        Snapshot snapshot = new Snapshot();
        snapshot.setMonitoringTask(task);
        snapshot.setStatus(SnapshotStatus.PENDING);
        snapshot.setSnapshotStartTime(startTime);
        snapshot.setSnapshotEndTime(endTime);

        return Optional.of(snapshot);
    }

    private void preloadApiKeys(List<MonitoringTask> tasks) {
        Set<UUID> keyIds = tasks.stream()
                .map(t -> t.getApiKey().getId())
                .collect(Collectors.toSet());
        apiKeyRepository.findAllById(keyIds);
    }
}

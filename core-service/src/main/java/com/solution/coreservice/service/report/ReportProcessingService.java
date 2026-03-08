package com.solution.coreservice.service.report;

import com.solution.coreservice.dto.messaging.ReportRequest;
import com.solution.coreservice.entity.MonitoringTask;
import com.solution.coreservice.entity.Snapshot;
import com.solution.coreservice.repository.MonitoringTaskRepository;
import com.solution.coreservice.repository.ReportRepository;
import com.solution.coreservice.repository.SnapshotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportProcessingService {

    private final SnapshotRepository snapshotRepository;
    private final MonitoringTaskRepository monitoringTaskRepository;
    private final ReportRepository reportRepository;

    public void initializeReport(MonitoringTask task) {

    }

    public void finalizeReport() {

    }

    public void startRequestReport(ReportRequest request) {

    }

    public void finalizeRequestReport() {

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

    public void handleFailure(MonitoringTask task) {

    }
}

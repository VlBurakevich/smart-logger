package com.solution.coreservice.scheduler;

import com.solution.coreservice.entity.MonitoringTask;
import com.solution.coreservice.repository.MonitoringTaskRepository;
import com.solution.coreservice.service.LogProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportScheduler {
    private final MonitoringTaskRepository monitoringTaskRepository;
    private final LogProcessingService logProcessingService;

    @Value("${app.scheduler.report.batch-size:10}")
    private int batchSize;

    @Scheduled(fixedRateString = "${app.scheduler.report.rate-ms}")
    public void scheduleReportProcessing() {
        List<MonitoringTask> tasks = monitoringTaskRepository.findReadyToReport(OffsetDateTime.now(), batchSize);

        for (MonitoringTask task : tasks) {
            try {
                logProcessingService.processReport(task);
            } catch (Exception e) {
                log.error("Critical error during task {} report", task.getId(), e);
            }
        }
    }
}

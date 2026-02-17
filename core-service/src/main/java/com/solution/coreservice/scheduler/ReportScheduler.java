package com.solution.coreservice.scheduler;

import com.solution.coreservice.repository.MonitoringTaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class ReportScheduler {
    private final MonitoringTaskRepository monitoringTaskRepository;

    @Value("${app.scheduler.report.batch-size:10}")
    private int batchSize;

    @Scheduled(fixedRateString = "${app.scheduler.report.rate-ms}")
    public void scheduleReportProcessing() {

    }
}

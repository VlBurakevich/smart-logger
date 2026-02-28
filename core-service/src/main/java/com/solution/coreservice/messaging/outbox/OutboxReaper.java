package com.solution.coreservice.messaging.outbox;

import com.solution.coreservice.service.OutboxService;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxReaper {
    private final OutboxService outboxService;

    @Value("${app.outbox.reaper.batch-size:50}")
    private Integer batchSize;

    @Scheduled(fixedRateString = "${app.outbox.reaper.delay:50000}")
    @SchedulerLock(
            name = "OutboxReaper_lock",
            lockAtMostFor = "10m",
            lockAtLeastFor = "50s"
    )
    public void reaper() {
        outboxService.resetStuckTasks(batchSize);
    }
}

package com.solution.coreservice.messaging.outbox;

import com.solution.coreservice.service.OutboxService;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class OutboxCleaner {
    private final OutboxService outboxService;

    @Scheduled(cron = "0 0 2 * * *")
    @SchedulerLock(
            name = "OutboxCleaner_lock",
            lockAtMostFor = "10m",
            lockAtLeastFor = "50s"
    )
    public void cleanupOldTasks() {
        outboxService.cleanupOldTasks();
    }
}

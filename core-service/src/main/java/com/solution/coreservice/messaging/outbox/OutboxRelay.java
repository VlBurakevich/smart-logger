package com.solution.coreservice.messaging.outbox;

import com.solution.coreservice.entity.OutboxMessage;
import com.solution.coreservice.entity.OutboxStatus;
import com.solution.coreservice.service.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxRelay {
    private final OutboxService outboxService;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value(value = "${app.outbox.relay.batch-size:50}")
    private int batchSize;

    @Scheduled(fixedRateString = "${app.outbox.relay.delay:5000}")
    @SchedulerLock(
            name = "OutboxRelay_lock",
            lockAtMostFor = "4m",
            lockAtLeastFor = "3s"
    )
    public void relay() {
        List<OutboxMessage> messages = outboxService.grabMessageForProcessing(batchSize);

        if (messages.isEmpty()) {
            return;
        }

        for (OutboxMessage message : messages) {
            sendToKafka(message);
        }
    }

    private void sendToKafka(OutboxMessage message) {
        kafkaTemplate.send(message.getTopic(), message.getId().toString(), message.getPayload().toString())
                .whenComplete((res, ex) -> {
                    if (ex == null) {
                        outboxService.updateFinalStatus(message.getId(), OutboxStatus.SENT, null);
                        log.info(">>>> [KAFKA] Send message from outbox to topic: {}", message.getTopic());
                    } else {
                        log.error(">>>> [KAFKA] to send outbox message {}: {}", message.getId(), ex.getMessage());
                        outboxService.updateFinalStatus(message.getId(), OutboxStatus.FAILED, ex.getMessage());
                    }
                });
    }
}

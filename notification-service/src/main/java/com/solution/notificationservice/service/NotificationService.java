package com.solution.notificationservice.service;

import com.solution.notificationservice.dto.SnapshotAlert;
import com.solution.notificationservice.entity.TelegramBinding;
import com.solution.notificationservice.events.SendTelegramMessageEvent;
import com.solution.notificationservice.repository.TelegramBindingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final ApplicationEventPublisher publisher;
    private final TelegramBindingRepository telegramBindingRepository;

    public void processAlert(SnapshotAlert alert) {
        String message = buildAlertMessage(alert);

        Set<Long> chatIds = telegramBindingRepository.findAllByUserId(alert.userId())
                .stream()
                .map(TelegramBinding::getChatId)
                .collect(Collectors.toSet());

        for (Long chatId : chatIds) {
            publisher.publishEvent(new SendTelegramMessageEvent(chatId, message));
        }
    }
    public void processReport() {

    }

    private String buildAlertMessage(SnapshotAlert alert) {
        int percentage = alert.aiScore().multiply(BigDecimal.valueOf(100)).intValue();
        String severity = percentage >= 80 ? "🔴" : (percentage >= 60 ? "🟡" : "🟢");

        return String.format("""
                 %s *Обнаружена проблема*
                 
                 *Сервис:* %s
                 *Критичность:* %s %d%%
                 *Причина:* %s
                 
                 %s
                 """,
                severity,
                alert.serviceName(),
                severity,
                percentage,
                alert.rootCause() != null ? alert.rootCause() : "Не определена",
                alert.aiDescription() != null ? alert.aiDescription() : ""
        );
    }

    private String buildReportMessage() {
        return "";
    }
}

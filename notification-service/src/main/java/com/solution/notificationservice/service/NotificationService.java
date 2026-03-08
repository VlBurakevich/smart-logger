package com.solution.notificationservice.service;

import com.solution.notificationservice.dto.ReportResult;
import com.solution.notificationservice.dto.SnapshotAlert;
import com.solution.notificationservice.entity.TelegramBinding;
import com.solution.notificationservice.events.SendTelegramMessageEvent;
import com.solution.notificationservice.repository.TelegramBindingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final ApplicationEventPublisher publisher;
    private final TelegramBindingRepository telegramBindingRepository;

    public void processAlert(SnapshotAlert alert) {
        String message = buildAlertMessage(alert);
        sendToTelegram(alert.userId(), message);
    }

    public void processReport(ReportResult reportResult) {
        String message = buildReportMessage(reportResult);
        sendToTelegram(reportResult.userId(), message);
    }

    private void sendToTelegram(UUID userId, String message) {
        Set<Long> chatIds = telegramBindingRepository.findAllByUserId(userId)
                .stream()
                .map(TelegramBinding::getChatId)
                .collect(Collectors.toSet());

        for (Long chatId : chatIds) {
            publisher.publishEvent(new SendTelegramMessageEvent(chatId, message));
        }
    }

    private String buildAlertMessage(SnapshotAlert alert) {
        int percentage = alert.aiScore().multiply(BigDecimal.valueOf(100)).intValue();
        String severity = getSeverity(percentage);

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

    private String getSeverity(int percentage) {
        if (percentage >= 80) return "🔴";
        if (percentage >= 60) return "🟡";
        return "🟢";
    }


    private String buildReportMessage(ReportResult reportResult) {
        int errorCount = reportResult.logLevelCount().getOrDefault("ERROR", 0);
        String header = errorCount > 0
                ? "⚠️ *Отчёт: обнаружены ошибки*"
                : "✅r *Отчёт: всё в порядке*";

        int total = reportResult.logLevelCount().values().stream()
                .mapToInt(Integer::intValue).sum();

        return String.format("""                                                                         
                 %s
                 
                 *Период:* %s
                 *Всего событий:* %d
                 *Ошибок:* %d
                 
                 *Логи:*
                 ERROR: %d
                 WARN: %d
                 INFO: %d
                 
                 *Итог:* %s
                 """,
                header,
                reportResult.period(),
                total,
                errorCount,
                errorCount,
                reportResult.logLevelCount().getOrDefault("WARN", 0),
                reportResult.logLevelCount().getOrDefault("INFO", 0),
                reportResult.summary() != null ? reportResult.summary() : "Нет данных"
        );
    }
}

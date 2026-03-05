package com.solution.notificationservice.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record TelegramChatResponse(
   UUID userId,
   Long chatId,
   String username,
   String firstName,
   OffsetDateTime createdAt,
   OffsetDateTime lastNotificationAt
) {
}

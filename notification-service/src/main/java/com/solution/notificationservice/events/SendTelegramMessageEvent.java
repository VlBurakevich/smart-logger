package com.solution.notificationservice.events;

public record SendTelegramMessageEvent(
        Long chatId,
        String text
) {
}

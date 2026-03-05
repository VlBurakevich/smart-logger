package com.solution.notificationservice.bot;

import com.solution.notificationservice.events.SendTelegramMessageEvent;
import com.solution.notificationservice.service.TelegramCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
public class TelegramBotHandler extends TelegramLongPollingBot {
    private final TelegramCommandService commandService;
    private final String botUsername;

    public TelegramBotHandler(
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.bot.username}") String botUsername,
            TelegramCommandService commandService
    ) {
        super(botToken);
        this.botUsername = botUsername;
        this.commandService = commandService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            commandService.processMessage(update.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @EventListener
    public void handleSendMessageEvent(SendTelegramMessageEvent event) {
        SendMessage sm = SendMessage.builder()
                .chatId(event.chatId())
                .text(event.text())
                .build();

        try {
            this.execute(sm);
        } catch (TelegramApiException e) {
            log.error("Error send message to user {}: {}", event.chatId(), e.getMessage());
        }
    }
}

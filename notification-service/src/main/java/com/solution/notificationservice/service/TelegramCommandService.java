package com.solution.notificationservice.service;

import com.solution.notificationservice.constants.MessageKeys;
import com.solution.notificationservice.events.SendTelegramMessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramCommandService {

    private final TelegramBindingService telegramBindingService;
    private final ApplicationEventPublisher publisher;
    private final MessageService messageService;

    private void publish(Long chatId, String messageKey, Object... args) {
        String text = messageService.getMessage(messageKey, args);
        publisher.publishEvent(new SendTelegramMessageEvent(chatId, text));
    }

    public void processMessage(Message message) {
        String text = message.getText();
        Long chatId = message.getChatId();
        User user = message.getFrom();

        String command = text.split("\\s+")[0].toLowerCase();

        switch (command) {
            case "/start" -> handleStart(chatId, text, user);
            case "/report" -> handleReport(chatId, user);
            case "/help" -> handleHelp(chatId);
            case "/stop", "/unbind" -> handleUnbind(chatId, user);
            default -> handleUnknown(chatId);
        }
    }

    public void handleStart(Long chatId, String messageText, User user) {
        String[] parts = messageText.split("\\s+");

        if (parts.length == 2) {
            publish(chatId, MessageKeys.BINDING_WELCOME);
            return;
        }

        try {
            telegramBindingService.validateAndBind(chatId, parts[1], user.getUserName(), user.getFirstName());
            publish(chatId, MessageKeys.BINDING_SUCCESS);

        } catch (IllegalArgumentException e) {
            publish(chatId, MessageKeys.BINDING_ERR_INVALID);
        } catch (Exception e) {
            log.error("Unexpected error during /start: ", e);
            publish(chatId, MessageKeys.BINDING_ERR_INTERNAL);
        }
    }

    private void handleReport(Long chatId, User user) {

    }

    private void handleHelp(Long chatId) {

    }

    private void handleUnbind(Long chatId, User user) {

    }

    private void handleUnknown(Long chatId) {

    }
}

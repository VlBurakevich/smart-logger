package com.solution.notificationservice.service;

import com.solution.notificationservice.client.CoreServiceClient;
import com.solution.notificationservice.constants.MessageKeys;
import com.solution.notificationservice.entity.TelegramBinding;
import com.solution.notificationservice.events.SendTelegramMessageEvent;
import com.solution.notificationservice.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramCommandService {

    private final TelegramBindingService telegramBindingService;
    private final MessageService messageService;
    private final CoreServiceClient coreServiceClient;
    private final ApplicationEventPublisher publisher;

    private void publish(Long chatId, String messageKey, Object... args) {
        String text = messageService.getMessage(messageKey, args);
        publisher.publishEvent(new SendTelegramMessageEvent(chatId, text));
    }

    public void processMessage(Message message) {
        String text = message.getText();
        Long chatId = message.getChatId();
        User user = message.getFrom();

        String[] parts = text.split("\\s+");
        String command = parts[0].toLowerCase();
        String[] args = parts.length > 1
                ? Arrays.copyOfRange(parts, 1, parts.length)
                : new String[0];

        switch (command) {
            case "/start" -> handleStart(chatId, text, user);
            case "/services" -> handleServices(chatId);
            case "/report" -> handleReport(chatId, user, args);
            case "/help" -> handleHelp(chatId);
            case "/stop", "/unbind" -> handleUnbind(chatId);
            default -> handleUnknown(chatId);
        }
    }

    public void handleStart(Long chatId, String messageText, User user) {
        String[] parts = messageText.split("\\s+");

        if (parts.length < 2) {
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

    private void handleServices(Long chatId) {
        UUID userId = telegramBindingService.getUserId(chatId);

        if (userId == null) {
            publish(chatId, MessageKeys.BINDING_REQUIRED);
            return;
        }
        try {
            List<String> serviceNames = coreServiceClient.getServiceNames(userId).serviceNames();

            if (serviceNames == null || serviceNames.isEmpty()) {
                publish(chatId, MessageKeys.SERVICES_EMPTY);
                return;
            }

            String message = String.join("\n", serviceNames);
            publisher.publishEvent(new SendTelegramMessageEvent(chatId, message));

        } catch (Exception e) {
            log.error("Error getting services for user {}: ", userId, e);
            publish(chatId, MessageKeys.ERROR_INTERNAL);
        }
    }

    private void handleReport(Long chatId, User user, String[] args) {

    }

    private void handleHelp(Long chatId) {
        String key = telegramBindingService.isBound(chatId)
                ? MessageKeys.HELP_AUTHORIZED
                : MessageKeys.HELP_UNAUTHORIZED;

        publish(chatId, key);
    }

    private void handleUnbind(Long chatId) {
        try {
            telegramBindingService.unbindByTelegramContext(chatId);
            publish(chatId, MessageKeys.UNBIND_SUCCESS);
        } catch (ServiceException e) {
            publish(chatId, MessageKeys.UNBIND_NOTFOUND);
        } catch (Exception e) {
            publish(chatId, MessageKeys.UNBIND_ERROR);
        }
    }

    private void handleUnknown(Long chatId) {
        publish(chatId, MessageKeys.UNKNOWN);
    }
}

package com.solution.notificationservice.service;

import com.solution.notificationservice.dto.TelegramChatResponse;
import com.solution.notificationservice.entity.TelegramBinding;
import com.solution.notificationservice.exception.ServiceException;
import com.solution.notificationservice.mapper.TelegramBindingMapper;
import com.solution.notificationservice.repository.TelegramBindingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramBindingService {
    private final TelegramBindingRepository telegramBindingRepository;
    private final TelegramBindingMapper telegramBindingMapper;
    private final RedisService redisService;

    @Value("${telegram.bot.link}")
    private String botLink;

    public List<TelegramChatResponse> getUserChats(UUID userId) {
        return telegramBindingMapper.toResponseList(
                telegramBindingRepository.findAllByUserId(userId));
    }

    public String generateBindingLink(UUID userId) {
        String token = UUID.randomUUID().toString().replace("-", "");
        redisService.save(token, userId);
        return botLink + "?start=" + token;
    }

    public boolean isBound(Long chatId) {
        return telegramBindingRepository.existsByChatId(chatId);
    }

    @Transactional
    public void validateAndBind(Long chatId, String token, String telegramUsername, String firstName) {
        UUID userId = redisService.consumeToken(token)
                .orElseThrow(() -> new IllegalArgumentException("The link vas invalid or expired"));

        if (telegramBindingRepository.existsByChatId(chatId)) {
            log.info("Chat already bound {}", chatId);
        }

        try {
            TelegramBinding binding = new TelegramBinding();
            binding.setChatId(chatId);
            binding.setUserId(userId);
            binding.setUsername(telegramUsername);
            binding.setFirstName(firstName);
            binding.setLastNotificationAt(OffsetDateTime.now(ZoneOffset.UTC));

            telegramBindingRepository.save(binding);
        } catch (Exception e) {
            log.error("Database error during building", e);
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "Database error during building");
        }
    }

    @Transactional
    public void unbind(UUID userId, Long chatId) {
        if (!telegramBindingRepository.existsByUserIdAndChatId(userId, chatId)) {
            throw new ServiceException(HttpStatus.NOT_FOUND, "Binding not found for this user");
        }

        telegramBindingRepository.deleteByUserIdAndChatId(userId, chatId);
    }

    @Transactional
    public void unbindByTelegramContext(Long chatId) {
        if (!telegramBindingRepository.existsByChatId(chatId)) {
            throw new ServiceException(HttpStatus.NOT_FOUND, "Binding not found");
        }

        telegramBindingRepository.deleteByChatId(chatId);
    }

    public UUID getUserId(Long chatId) {
        return telegramBindingRepository.findUserIdByChatId(chatId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "User not found"));
    }
}

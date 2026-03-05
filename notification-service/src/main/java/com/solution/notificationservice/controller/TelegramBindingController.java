package com.solution.notificationservice.controller;

import com.solution.notificationservice.dto.TelegramChatResponse;
import com.solution.notificationservice.service.TelegramBindingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification/telegram")
public class TelegramBindingController {

    private final TelegramBindingService telegramBindingService;

    @GetMapping("/chats")
    public ResponseEntity<List<TelegramChatResponse>> getUserChats(
            @RequestHeader("User-Id") UUID userId
    ) {
        return ResponseEntity.ok().body(telegramBindingService.getUserChats(userId));
    }

    @PostMapping("/link")
    public ResponseEntity<String> generateLink(
            @RequestHeader("User-Id") UUID userId
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(telegramBindingService.generateBindingLink(userId));
    }

    @DeleteMapping("/chats/{chatId}")
    public ResponseEntity<Void> unbindChat(
        @RequestHeader("User-Id") UUID userId,
        @PathVariable Long chatId
    ) {
        telegramBindingService.unbind(userId, chatId);
        return ResponseEntity.noContent().build();
    }
}

package com.solution.notificationservice.repository;

import com.solution.notificationservice.entity.TelegramBinding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TelegramBindingRepository extends JpaRepository<TelegramBinding, UUID> {

    List<TelegramBinding> findAllByUserId(UUID userId);

    boolean existsByUserIdAndChatId(UUID userId, Long chatId);

    void deleteByUserIdAndChatId(UUID userId, Long chatId);

    boolean existsByChatId(Long chatId);
}

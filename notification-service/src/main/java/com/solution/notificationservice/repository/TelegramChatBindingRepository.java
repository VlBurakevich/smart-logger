package com.solution.notificationservice.repository;

import com.solution.notificationservice.entity.TelegramChatBinding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TelegramChatBindingRepository extends JpaRepository<TelegramChatBinding, UUID> {

}

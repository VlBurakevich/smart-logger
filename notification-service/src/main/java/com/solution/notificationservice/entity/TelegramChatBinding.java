package com.solution.notificationservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "telegram_chat_bindings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TelegramChatBinding {
    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "chat_id", nullable = false, unique = true)
    private Long chatId;

    @Column(name = "username")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "last_notification_at")
    private OffsetDateTime lastNotificationAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TelegramChatBinding that = (TelegramChatBinding) o;
        return userId != null && userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "TelegramChatBinding{" +
                "userId=" + userId +
                ", chatId=" + chatId +
                ", username=" + username +
                ", firstName=" + firstName +
                ", createdAt=" + createdAt +
                ", lastNotificationAt" + lastNotificationAt +
                "}";
    }
}

package com.solution.notificationservice.service;


import com.solution.notificationservice.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate redis;

    @Value("${app.redis.tg-token-ttl:15m}")
    private Duration tgTokenTtl;

    private static final String TOKEN_PREFIX = "tg_token:";

    public void save(String tgToken, UUID userId) {
        if (userId == null) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED, "Missing user identity");
        }

        if (tgToken == null || tgToken.isBlank()) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Token cannot be null or empty");
        }

        redis.opsForValue().set(TOKEN_PREFIX + tgToken, userId.toString(), tgTokenTtl);
    }

    public Optional<UUID> consumeToken(String tgToken) {
        String key = TOKEN_PREFIX + tgToken;
        String userIdRaw = redis.opsForValue().get(key);

        if (userIdRaw == null) {
            return Optional.empty();
        }

        redis.delete(key);

        return Optional.of(UUID.fromString(userIdRaw));
    }
}

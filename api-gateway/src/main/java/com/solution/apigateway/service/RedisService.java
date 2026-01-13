package com.solution.apigateway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate redis;

    private static final String PREFIX = "account:";

    public boolean exists(String accountId) {
        return Boolean.TRUE.equals(redis.hasKey(PREFIX + accountId));
    }

    public void save(String accountId, Duration ttl) {
        redis.opsForValue().set(PREFIX + accountId, "1", ttl);
    }

    public void extendTtl(String accountId, Duration ttl) {
        if (accountId == null || accountId.isBlank()) {
            return;
        }
        redis.expire(PREFIX + accountId, ttl);
    }
}

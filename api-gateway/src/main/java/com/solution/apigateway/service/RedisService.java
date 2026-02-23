package com.solution.apigateway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate redis;

    private static final String KEY_PREFIX = "apiKey:";

    public boolean exists(String apiKey) {
        return Boolean.TRUE.equals(redis.hasKey(KEY_PREFIX + apiKey));
    }

    public void save(String apiKey, Duration ttl) {
        redis.opsForValue().set(KEY_PREFIX + apiKey, "1", ttl);
    }

    public void extendTtl(String apiKey, Duration ttl) {
        if (apiKey == null || apiKey.isBlank()) {
            return;
        }
        redis.expire(KEY_PREFIX + apiKey, ttl);
    }
}

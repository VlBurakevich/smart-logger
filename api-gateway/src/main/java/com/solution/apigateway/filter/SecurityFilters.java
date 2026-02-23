package com.solution.apigateway.filter;

import com.solution.apigateway.client.CoreServiceClient;
import com.solution.apigateway.service.JwtService;
import com.solution.apigateway.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityFilters {

    private final JwtService jwtService;
    private final RedisService redisService;
    private final CoreServiceClient coreClient;

    @Value("${app.redis.api-key-ttl:15m}")
    private Duration apiKeyTtl;


    private static final String API_KEY_HEADER = "Api-Key";
    private static final String API_KEY_HASH_HEADER = "Api-Key-Hash";

    public HandlerFilterFunction<ServerResponse, ServerResponse> logging() {
        return (request, next) -> {
            long startTime = System.currentTimeMillis();

            log.info("==> Incoming: {} {}", request.method(), request.path());

            try {
                ServerResponse response = next.handle(request);
                long duration = System.currentTimeMillis() - startTime;

                log.info("<== Outgoing: {} {}, Status: {}, Time: {}ms",
                        request.method(), request.path(), response.statusCode().value(), duration);

                return response;
            } catch (Exception e) {
                log.error("<== Failed: {} {}, Error: {}ms", request.method(), request.path(), e.getMessage());
                throw e;
            }
        };
    }

    public HandlerFilterFunction<ServerResponse, ServerResponse> jwtAuth() {
        return (request, next) -> {
            String authHeader = request.headers().firstHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
            }

            String token = authHeader.substring(7);

            try {
                UUID userId = jwtService.extractUserId(token).orElse(null);
                if (userId != null) {
                    ServerRequest modified = ServerRequest.from(request)
                            .header("User-Id", userId.toString())
                            .header("User-Roles", String.join(",", jwtService.extractRoles(token)))
                            .build();

                    return next.handle(modified);
                }
            } catch (Exception e) {
                log.error("JWT validation failed: {}", e.getMessage());
            }

            return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
        };
    }

    public HandlerFilterFunction<ServerResponse, ServerResponse> apiKey() {
        return (request, next) -> {
            String apiKey = request.headers().firstHeader(API_KEY_HEADER);

            if (apiKey == null || apiKey.isBlank()) {
                return ServerResponse.status(HttpStatus.FORBIDDEN).build();
            }

            String apiKeyHash = DigestUtils.sha256Hex(apiKey);

            if (!redisService.exists(apiKeyHash)) {
                if (!coreClient.apiKeyExists(apiKeyHash)) {
                    return ServerResponse.status(HttpStatus.FORBIDDEN).build();
                }
                redisService.save(apiKeyHash, apiKeyTtl);
            } else {
                redisService.extendTtl(apiKeyHash, apiKeyTtl);
            }

            ServerRequest modified = ServerRequest.from(request)
                    .header(API_KEY_HASH_HEADER, apiKeyHash)
                    .headers(headers -> headers.remove(API_KEY_HEADER))
                    .build();

            return next.handle(modified);
        };
    }
}
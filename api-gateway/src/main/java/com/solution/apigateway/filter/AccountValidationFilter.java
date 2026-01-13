package com.solution.apigateway.filter;

import com.solution.apigateway.client.CoreServiceClient;
import com.solution.apigateway.service.RedisService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class AccountValidationFilter extends OncePerRequestFilter {

    private final RedisService redisService;
    private final CoreServiceClient coreClient;

    private static final String ACCOUNT_ID_HEADER = "X-Account-Id";

    @Value("${app.redis.account-ttl:15m}")
    private Duration accountTtl;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/api/ingestion");
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String accountId = request.getHeader(ACCOUNT_ID_HEADER);

        if (accountId == null || accountId.isBlank()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if (!redisService.exists(accountId)) {
            if (!coreClient.accountExists(accountId)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            redisService.save(accountId, accountTtl);
        } else {
            redisService.extendTtl(accountId, accountTtl);
        }

        filterChain.doFilter(request, response);
    }
}

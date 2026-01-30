package com.solution.authservice.service;

import com.solution.authservice.entity.RefreshToken;
import com.solution.authservice.entity.User;
import com.solution.authservice.exception.ServiceException;
import com.solution.authservice.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class  RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-token.lifetime}")
    private Duration lifetime;

    @Scheduled(fixedRateString = "${jwt.refresh-token.cleanup-interval}")
    @Transactional
    public void purgeExpiredTokens() {
        refreshTokenRepository.deleteByExpiresAtBefore(OffsetDateTime.now());
    }

    @Transactional
    public RefreshToken create(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(OffsetDateTime.now().plus(lifetime));

        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public RefreshToken rotate(UUID oldTokenValue) {
        RefreshToken oldToken = refreshTokenRepository.findById(oldTokenValue)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Old refresh token not found"));

        if (oldToken.isExpired()) {
            refreshTokenRepository.delete(oldToken);
            throw new ServiceException(HttpStatus.NOT_FOUND, "Failed delete old refresh token");
        }

        User user = oldToken.getUser();

        refreshTokenRepository.delete(oldToken);

        return create(user);
    }

    @Transactional
    public void revokeForUser(UUID tokenId, UUID userId) {
        RefreshToken refreshToken = refreshTokenRepository.findById(tokenId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Refresh Token not found"));

        if (!refreshToken.getUser().getId().equals(userId)) {
            throw new ServiceException(HttpStatus.FORBIDDEN, "Access denied");
        }

        refreshTokenRepository.delete(refreshToken);
    }

    @Transactional
    public void revokeAllForUser(UUID userId) {
        refreshTokenRepository.deleteAllByUserId(userId);
    }
}

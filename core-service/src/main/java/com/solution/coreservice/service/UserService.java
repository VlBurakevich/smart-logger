package com.solution.coreservice.service;

import com.solution.coreservice.dto.request.RegisterRequest;
import com.solution.coreservice.dto.request.StatusUpdateRequest;
import com.solution.coreservice.entity.User;
import com.solution.coreservice.exception.ServiceException;
import com.solution.coreservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.existsById(request.getId())) {
            throw new ServiceException(HttpStatus.CONFLICT, "User ID already exists");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ServiceException(HttpStatus.CONFLICT, "Username is already taken");
        }

        try {
            User user = new User();
            user.setUsername(request.getUsername());
            user.setId(request.getId());
            user.setIsActive(true);

            userRepository.save(user);
        } catch (Exception e) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "Core-service database error: " + e.getMessage());
        }
    }

    @Transactional
    public void setIsActive(UUID userId, StatusUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "User not found"));

        user.setIsActive(request.getIsActive());
        userRepository.save(user);
    }

    @Transactional
    public void deleteAccount(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new ServiceException(HttpStatus.NOT_FOUND, "User not found");
        }
        userRepository.deleteById(userId);
    }
}

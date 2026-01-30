package com.solution.authservice.service;

import com.solution.authservice.client.CoreServiceClient;
import com.solution.authservice.dto.request.StatusUpdateRequest;
import com.solution.authservice.dto.response.UserResponse;
import com.solution.authservice.entity.User;
import com.solution.authservice.exception.ServiceException;
import com.solution.authservice.mapper.UserResponseMapper;
import com.solution.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final UserResponseMapper userResponseMapper;
    private final CoreServiceClient coreServiceClient;

    public Page<UserResponse> getAllUsers(Pageable pageable) {
        Page<User> userPage = userRepository.findAllWithDetails(pageable);
        return userPage.map(userResponseMapper::toResponse);
    }

    @Transactional
    public void updateStatus(UUID userId, StatusUpdateRequest statusUpdateRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(HttpStatus.UNAUTHORIZED, User.class.getName()));

        user.setActive(statusUpdateRequest.getIsActive());
        coreServiceClient.setIsActive(userId, statusUpdateRequest);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new ServiceException(HttpStatus.NOT_FOUND, "Failed delete user");
        }
        log.info("Send feignClient Delete user {}", userId);
        coreServiceClient.deleteUserById(userId);
        userRepository.deleteById(userId);
    }
}

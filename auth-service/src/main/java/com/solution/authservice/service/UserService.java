package com.solution.authservice.service;

import com.solution.authservice.dto.request.ChangePasswordRequest;
import com.solution.authservice.dto.response.UserResponse;
import com.solution.authservice.entity.Credential;
import com.solution.authservice.entity.User;
import com.solution.authservice.exception.ServiceException;
import com.solution.authservice.mapper.UserResponseMapper;
import com.solution.authservice.repository.CredentialRepository;
import com.solution.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CredentialRepository credentialRepository;
    private final UserResponseMapper userResponseMapper;
    private final PasswordEncoder passwordEncoder;

    public UserResponse getUserInfo(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "User not found"));

        return userResponseMapper.toResponse(user);
    }

    @Transactional
    public void changePassword(UUID id, ChangePasswordRequest request) {
        Credential credential = credentialRepository.findByUserId(id)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Credential not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), credential.getPasswordHash())) {
            throw new ServiceException(HttpStatus.FORBIDDEN, "Old password not match");
        }

        if (passwordEncoder.matches(request.getNewPassword(), credential.getPasswordHash())) {
            throw new ServiceException(HttpStatus.FORBIDDEN, "New password must be different");
        }

        String newHash = passwordEncoder.encode(request.getNewPassword());
        credential.setPasswordHash(newHash);

        credentialRepository.save(credential);
    }
}

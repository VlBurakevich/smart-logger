package com.solution.authservice.service;

import com.solution.authservice.client.CoreServiceClient;
import com.solution.authservice.dto.request.LogoutRequest;
import com.solution.authservice.dto.request.RefreshRequest;
import com.solution.authservice.dto.request.RegisterCoreRequest;
import com.solution.authservice.dto.request.LoginRequest;
import com.solution.authservice.dto.request.RegisterRequest;
import com.solution.authservice.dto.response.AuthResponse;
import com.solution.authservice.dto.response.RegisterResponse;
import com.solution.authservice.entity.Credential;
import com.solution.authservice.entity.RefreshToken;
import com.solution.authservice.entity.Role;
import com.solution.authservice.entity.User;
import com.solution.authservice.exception.ServiceException;
import com.solution.authservice.security.JwtService;
import com.solution.authservice.security.UserPrincipal;
import com.solution.authservice.mapper.RegisterResponseMapper;
import com.solution.authservice.repository.RoleRepository;
import com.solution.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RegisterResponseMapper registerResponseMapper;
    private final RefreshTokenService refreshTokenService;
    private final CoreServiceClient coreServiceClient;

    @Transactional
    public RegisterResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new ServiceException(HttpStatus.CONFLICT, "Username already exists");
        }

        User user = createNewUser(registerRequest);
        userRepository.save(user);

        saveUserInCoreService(user);

        String accessToken = jwtService.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.create(user);

        RegisterResponse response = registerResponseMapper.toResponse(user);
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken.getToken().toString());

        return response;
    }

    private void saveUserInCoreService(User user) {
        RegisterCoreRequest request = new RegisterCoreRequest();
        request.setId(user.getId());
        request.setUsername(user.getUsername());
        coreServiceClient.register(request);
    }

    private User createNewUser(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setActive(true);

        user.addRole(getUserRole());

        Credential credential = new Credential();
        credential.setPasswordHash(passwordEncoder.encode(registerRequest.getPassword()));
        credential.setEmail(registerRequest.getEmail());
        credential.setUser(user);

        user.setCredential(credential);

        return user;
    }

    private Role getUserRole() {
        return roleRepository.findByName(Role.USER)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Role not found"));
    }

    @Transactional
    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername()).orElse(null);
        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getCredential().getPasswordHash())) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }

        AuthResponse response = new AuthResponse();
        response.setAccessToken(jwtService.generateAccessToken(user));
        response.setRefreshToken(refreshTokenService.create(user).getToken().toString());

        return response;
    }

    @Transactional
    public void logout(LogoutRequest logoutRequest, UserPrincipal userPrincipal) {
        refreshTokenService.revokeForUser(
                UUID.fromString(logoutRequest.getRefreshToken()),
                userPrincipal.getId()
        );
    }

    @Transactional
    public AuthResponse refresh(RefreshRequest refreshRequest) {
        RefreshToken newToken = refreshTokenService.rotate(UUID.fromString(refreshRequest.getRefreshToken()));

        String accessToken = jwtService.generateAccessToken(newToken.getUser());

        return new AuthResponse(accessToken, newToken.getToken().toString());
    }
}

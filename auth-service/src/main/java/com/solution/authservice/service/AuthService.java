package com.solution.authservice.service;

import com.solution.authservice.dto.request.LogoutRequest;
import com.solution.authservice.dto.request.RefreshRequest;
import com.solution.authservice.dto.request.UserLoginRequest;
import com.solution.authservice.dto.request.UserRegisterRequest;
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
import com.solution.authservice.repository.RefreshTokenRepository;
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
    private final RefreshTokenRepository refreshTokenRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RegisterResponseMapper registerResponseMapper;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public RegisterResponse register(UserRegisterRequest userRegisterRequest) { //todo create user in core-service
        if (userRepository.existsByUsername(userRegisterRequest.getUsername())) {
            throw new ServiceException(HttpStatus.CONFLICT, "Username already exists");
        }

        User user = createNewUser(userRegisterRequest);
        userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.create(user);

        RegisterResponse response = registerResponseMapper.toResponse(user);
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken.getToken().toString());

        return registerResponseMapper.toResponse(user);
    }

    private User createNewUser(UserRegisterRequest userRegisterRequest) {
        User user = new User();
        user.setUsername(userRegisterRequest.getUsername());
        user.setActive(true);

        user.addRole(getUserRole());

        Credential credential = new Credential();
        credential.setPasswordHash(passwordEncoder.encode(userRegisterRequest.getPassword()));
        credential.setEmail(userRegisterRequest.getEmail());
        credential.setUser(user);

        return user;
    }

    private Role getUserRole() {
        return roleRepository.findByName(Role.USER)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Role not found"));
    }

    @Transactional
    public AuthResponse login(UserLoginRequest userLoginRequest) {
        User user = userRepository.findByUsername(userLoginRequest.getUsername()).orElse(null);
        if (user == null || !passwordEncoder.matches(userLoginRequest.getPassword(), user.getCredential().getPasswordHash())) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }

        AuthResponse response = new AuthResponse();
        response.setAccessToken(jwtService.generateAccessToken(user));
        response.setRefreshToken(refreshTokenService.create(user).getToken().toString());

        return response;
    }

    @Transactional
    public void logout(LogoutRequest logoutRequest, UserPrincipal userPrincipal) {
        UUID tokenUuid = UUID.fromString(logoutRequest.getRefreshToken());

        RefreshToken refreshToken = refreshTokenRepository.findById(tokenUuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Refresh token not found"));

        if (!refreshToken.getUser().getId().equals(userPrincipal.getId())) {
            throw new ServiceException(HttpStatus.FORBIDDEN, "Access denied: You cannot revoke someone else's token");
        }

        refreshTokenRepository.delete(refreshToken);
    }

    @Transactional
    public AuthResponse refresh(RefreshRequest refreshRequest) {
        RefreshToken newToken = refreshTokenService.rotate(UUID.fromString(refreshRequest.getRefreshToken()));

        String accessToken = jwtService.generateAccessToken(newToken.getUser());

        return new AuthResponse(accessToken, newToken.getToken().toString());
    }
}

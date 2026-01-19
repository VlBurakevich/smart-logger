package com.solution.authservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {
    @NotBlank(message = "Username cannot be empty")
    private String username;

    @Email(message = "Invalid email address")
    private String email;

    @Email(message = "Password cannot be empty")
    private String password;
}

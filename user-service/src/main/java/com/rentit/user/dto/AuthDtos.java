package com.rentit.user.dto;

import com.rentit.user.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

public class AuthDtos {

    @Data
    public static class RegisterRequest {
        @NotBlank
        @Email
        private String email;
        @NotBlank
        @Size(min = 6, max = 100)
        private String password;
        @NotBlank
        private String firstName;
        @NotBlank
        private String lastName;
        private String phoneNumber;
        private String description;
        @NotEmpty
        private Set<Role> roles;
    }

    @Data
    public static class LoginRequest {
        @NotBlank
        @Email
        private String email;
        @NotBlank
        private String password;
    }

    @Data
    public static class AuthResponse {
        private String accessToken;
        private Long userId;
        private String email;
        private String firstName;
        private String lastName;
        private Set<Role> roles;
    }
} 
package com.rentit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {
    private Long id;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 2, max = 50)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 50)
    private String lastName;

    @Size(min = 10, max = 15)
    private String phoneNumber;

    private String description;
    private Double rating;
    private boolean isVerified;
} 
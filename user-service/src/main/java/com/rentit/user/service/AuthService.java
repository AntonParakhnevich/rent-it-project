package com.rentit.user.service;

import com.rentit.user.dto.AuthDtos;
import com.rentit.user.model.User;
import com.rentit.user.repository.UserRepository;
import com.rentit.user.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthDtos.AuthResponse register(AuthDtos.RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setDescription(request.getDescription());
        user.setRoles(request.getRoles());
        User saved = userRepository.save(user);

        AuthDtos.AuthResponse response = new AuthDtos.AuthResponse();
        response.setAccessToken(jwtService.generateToken(saved.getEmail(), new HashMap<>()));
        response.setUserId(saved.getId());
        response.setEmail(saved.getEmail());
        response.setFirstName(saved.getFirstName());
        response.setLastName(saved.getLastName());
        response.setRoles(saved.getRoles());
        return response;
    }

    public AuthDtos.AuthResponse login(AuthDtos.LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        AuthDtos.AuthResponse response = new AuthDtos.AuthResponse();
        response.setAccessToken(jwtService.generateToken(user.getEmail(), new HashMap<>()));
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setRoles(user.getRoles());
        return response;
    }
} 
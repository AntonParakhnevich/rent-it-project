package com.rentit.privatearea.service;

import com.rentit.privatearea.security.JwtService;
import com.rentit.user.api.AuthResponse;
import com.rentit.user.api.LoginRequest;
import com.rentit.user.api.UserConnector;
import com.rentit.user.api.UserCreateRequest;
import com.rentit.user.api.UserResponse;
import java.util.HashMap;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final UserConnector userConnector;
  private final PasswordEncoder passwordEncoder;

  public AuthResponse register(UserCreateRequest request) {
    request.setPassword(passwordEncoder.encode(request.getPassword()));
    UserResponse saved = userConnector.create(request);

    AuthResponse response = new AuthResponse();
    response.setAccessToken(jwtService.generateToken(saved.getEmail(), new HashMap<>()));
    response.setUserId(saved.getId());
    response.setEmail(saved.getEmail());
    response.setFirstName(saved.getFirstName());
    response.setLastName(saved.getLastName());
    return response;
  }

  public AuthResponse login(LoginRequest request) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
    );
    UserResponse user = Optional.ofNullable(userConnector.getByEmail(request.getEmail()))
        .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

    AuthResponse response = new AuthResponse();
    response.setAccessToken(jwtService.generateToken(user.getEmail(), new HashMap<>()));
    response.setUserId(user.getId());
    response.setEmail(user.getEmail());
    response.setFirstName(user.getFirstName());
    response.setLastName(user.getLastName());
    return response;
  }
} 
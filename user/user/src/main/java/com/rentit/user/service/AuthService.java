package com.rentit.user.service;

import com.rentit.user.api.AuthResponse;
import com.rentit.user.api.LoginRequest;
import com.rentit.user.api.RegisterRequest;
import com.rentit.user.model.Role;
import com.rentit.user.model.User;
import com.rentit.user.repository.UserRepository;
import com.rentit.user.security.JwtService;
import java.util.HashMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  @Transactional
  public AuthResponse register(RegisterRequest request) {
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
    user.setRoles(request.getRoles().stream().map(Role::valueOf).collect(Collectors.toSet()));
    User saved = userRepository.save(user);

    AuthResponse response = new AuthResponse();
    response.setAccessToken(jwtService.generateToken(saved.getEmail(), new HashMap<>()));
    response.setUserId(saved.getId());
    response.setEmail(saved.getEmail());
    response.setFirstName(saved.getFirstName());
    response.setLastName(saved.getLastName());
    response.setRoles(saved.getRoles().stream().map(Enum::name).collect(Collectors.toSet()));
    return response;
  }

  public AuthResponse login(LoginRequest request) {
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
    );
    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

    AuthResponse response = new AuthResponse();
    response.setAccessToken(jwtService.generateToken(user.getEmail(), new HashMap<>()));
    response.setUserId(user.getId());
    response.setEmail(user.getEmail());
    response.setFirstName(user.getFirstName());
    response.setLastName(user.getLastName());
    response.setRoles(user.getRoles().stream().map(Enum::name).collect(Collectors.toSet()));
    return response;
  }
} 
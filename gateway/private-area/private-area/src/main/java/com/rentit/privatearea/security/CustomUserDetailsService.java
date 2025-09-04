package com.rentit.privatearea.security;

import com.rentit.user.api.UserConnector;
import com.rentit.user.api.UserLoginResponse;
import com.rentit.user.api.UserResponse;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserConnector userConnector;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    UserLoginResponse user = Optional.ofNullable(userConnector.getByEmail(email))
        .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    Set<GrantedAuthority> authorities = user.getRoles().stream()
        .map(this::mapRoleToAuthority)
        .collect(Collectors.toSet());

    return User
        .withUsername(user.getEmail())
        .password(user.getPassword())
        .authorities(authorities)
        .accountExpired(false)
        .accountLocked(false)
        .credentialsExpired(false)
        .build();
  }

  private GrantedAuthority mapRoleToAuthority(String role) {
    return new SimpleGrantedAuthority("ROLE_" + role);
  }
} 
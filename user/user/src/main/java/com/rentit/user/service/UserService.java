package com.rentit.user.service;

import com.rentit.user.api.UserCreateRequest;
import com.rentit.user.dto.UserDto;
import com.rentit.user.model.User;
import com.rentit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public UserDto createUser(UserCreateRequest request) {
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
    User savedUser = userRepository.save(user);
    return convertToDto(savedUser);
  }

  @Transactional(readOnly = true)
  public UserDto getUserById(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    return convertToDto(user);
  }

  @Transactional
  public UserDto updateUser(Long id, UserDto userDto) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    user.setFirstName(userDto.getFirstName());
    user.setLastName(userDto.getLastName());
    user.setPhoneNumber(userDto.getPhoneNumber());
    user.setDescription(userDto.getDescription());
    User updated = userRepository.save(user);
    return convertToDto(updated);
  }

  @Transactional
  public void deleteUser(Long id) {
    if (!userRepository.existsById(id)) {
      throw new RuntimeException("Пользователь не найден");
    }
    userRepository.deleteById(id);
  }

  private UserDto convertToDto(User user) {
    UserDto dto = new UserDto();
    dto.setId(user.getId());
    dto.setEmail(user.getEmail());
    dto.setFirstName(user.getFirstName());
    dto.setLastName(user.getLastName());
    dto.setPhoneNumber(user.getPhoneNumber());
    dto.setDescription(user.getDescription());
    dto.setRating(user.getRating());
    dto.setVerified(user.isVerified());
    return dto;
  }
} 
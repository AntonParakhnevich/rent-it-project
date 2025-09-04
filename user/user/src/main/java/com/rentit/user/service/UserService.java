package com.rentit.user.service;

import com.rentit.user.api.UserCreateRequest;
import com.rentit.user.api.UserResponse;
import com.rentit.user.dto.UserDto;
import com.rentit.user.model.Role;
import com.rentit.user.model.User;
import com.rentit.user.repository.UserRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  @Transactional
  public UserResponse createUser(UserCreateRequest request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new RuntimeException("Пользователь с таким email уже существует");
    }
    User user = new User();
    user.setEmail(request.getEmail());
    user.setPassword(request.getPassword());
    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setPhoneNumber(request.getPhoneNumber());
    user.setDescription(request.getDescription());
    user.setRoles(request.getRoles().stream().map(Role::valueOf).collect(Collectors.toSet()));
    User savedUser = userRepository.save(user);
    return convertToDto(savedUser);
  }

  @Transactional(readOnly = true)
  public UserResponse getUserById(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    return convertToDto(user);
  }

  @Transactional(readOnly = true)
  public UserResponse getByEmail(String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    return convertToDto(user);
  }

  @Transactional
  public UserResponse updateUser(Long id, UserDto userDto) {
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

  private UserResponse convertToDto(User user) {
    UserResponse dto = new UserResponse();
    dto.setId(user.getId());
    dto.setEmail(user.getEmail());
    dto.setFirstName(user.getFirstName());
    dto.setLastName(user.getLastName());
    dto.setPhoneNumber(user.getPhoneNumber());
    dto.setDescription(user.getDescription());
    dto.setVerified(user.isVerified());
    dto.setPassword(user.getPassword());
    dto.setEnabled(user.isEnabled());
    dto.setRoles(user.getRoles().stream().map(Enum::name).collect(Collectors.toSet()));
    return dto;
  }
} 
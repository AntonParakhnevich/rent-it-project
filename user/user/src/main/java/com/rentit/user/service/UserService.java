package com.rentit.user.service;

import com.rentit.tax.api.TaxConnector;
import com.rentit.tax.api.TaxRequest;
import com.rentit.tax.api.TaxResponse;
import com.rentit.user.api.UserCreateRequest;
import com.rentit.user.api.UserLoginResponse;
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
  private final UserRatingService userRatingService;
  private final TaxConnector taxConnector;

  @Transactional
  public UserResponse createUser(UserCreateRequest request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new RuntimeException("Пользователь с таким email уже существует");
    }
    checkUnpIfNeeded(request);
    User user = new User();
    user.setEmail(request.getEmail());
    user.setPassword(request.getPassword());
    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setPhoneNumber(request.getPhoneNumber());
    user.setDescription(request.getDescription());
    user.setUnp(request.getUnp());
    user.setRoles(request.getRoles().stream().map(Role::valueOf).collect(Collectors.toSet()));
    User savedUser = userRepository.save(user);
    return convertToUserResponse(savedUser);
  }

  @Transactional(readOnly = true)
  public UserResponse getUserById(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Пользователь не найден, id=" + id));
    UserResponse userResponse = convertToUserResponse(user);
    userResponse.setRating(userRatingService.getAverageByUserId(id));
    return userResponse;
  }

  @Transactional(readOnly = true)
  public UserLoginResponse getByEmail(String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Пользователь не найден, email=" + email));
    UserLoginResponse userLoginResponse = new UserLoginResponse();
    userLoginResponse.setId(user.getId());
    userLoginResponse.setEmail(user.getEmail());
    userLoginResponse.setFirstName(user.getFirstName());
    userLoginResponse.setLastName(user.getLastName());
    userLoginResponse.setRoles(user.getRoles().stream().map(Enum::name).collect(Collectors.toSet()));
    userLoginResponse.setPassword(user.getPassword());
    return userLoginResponse;
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
    return convertToUserResponse(updated);
  }

  @Transactional
  public void deleteUser(Long id) {
    if (!userRepository.existsById(id)) {
      throw new RuntimeException("Пользователь не найден");
    }
    userRepository.deleteById(id);
  }

  private UserResponse convertToUserResponse(User user) {
    UserResponse dto = new UserResponse();
    dto.setId(user.getId());
    dto.setEmail(user.getEmail());
    dto.setFirstName(user.getFirstName());
    dto.setLastName(user.getLastName());
    dto.setPhoneNumber(user.getPhoneNumber());
    dto.setDescription(user.getDescription());
    dto.setUnp(user.getUnp());
    dto.setVerified(user.isVerified());
    dto.setEnabled(user.isEnabled());
    dto.setRoles(user.getRoles().stream().map(Enum::name).collect(Collectors.toSet()));
    return dto;
  }

  private void checkUnpIfNeeded(UserCreateRequest request) {
    if (request.getRoles().contains(Role.LANDLORD.name())) {
      TaxRequest taxRequest = new TaxRequest();
      taxRequest.setFirstName(request.getFirstName());
      taxRequest.setLastName(request.getLastName());
      taxRequest.setUnp(request.getUnp());
      TaxResponse response = taxConnector.check(taxRequest);
      if (!response.isValid()) {
        throw new IllegalArgumentException("Unp invalid, unp=" + request.getUnp());
      }
    }
  }
} 
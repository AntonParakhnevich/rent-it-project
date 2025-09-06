package com.rentit.user.controller;

import com.rentit.user.api.UserCreateRequest;
import com.rentit.user.api.UserLoginResponse;
import com.rentit.user.api.UserResponse;
import com.rentit.user.dto.UserDto;
import com.rentit.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping
  public UserResponse createUser(@Valid @RequestBody UserCreateRequest request) {
    return userService.createUser(request);
  }

  @GetMapping("/{id}")
  public UserResponse getUser(@PathVariable Long id) {
    return userService.getUserById(id);
  }

  @GetMapping()
  public UserLoginResponse getByEmail(@RequestParam("email") String email) {
    return userService.getByEmail(email);
  }

  @PutMapping("/{id}")
  public UserResponse updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
    return userService.updateUser(id, userDto);
  }

  @DeleteMapping("/{id}")
  public void deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
  }
} 
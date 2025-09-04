package com.rentit.privatearea.controller;

import com.rentit.privatearea.service.user.UserService;
import com.rentit.user.api.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> getById(@PathVariable("id") Long id) {
    return ResponseEntity.ok(userService.getById(id));
  }
}

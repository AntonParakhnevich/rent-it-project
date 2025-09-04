package com.rentit.user.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "userConnector", url = "http://localhost:8080") // Or use service name for Eureka
public interface UserConnector {

  @GetMapping("/users/{id}")
  UserResponse getUserById(@PathVariable("id") Long id);

  @GetMapping("/users?email={email}")
  UserLoginResponse getByEmail(@PathVariable("email") String email);

  @PostMapping("/users")
  UserResponse create(UserCreateRequest request);

}

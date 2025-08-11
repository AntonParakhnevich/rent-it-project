package com.rentit.user.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "userConnector", url = "http://localhost:8081") // Or use service name for Eureka
public interface UserConnector {

  @GetMapping("/api/users/{id}")
  UserResponse getUserById(@PathVariable("id") Long id);

}

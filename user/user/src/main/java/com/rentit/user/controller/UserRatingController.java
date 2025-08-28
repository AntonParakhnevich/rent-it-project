package com.rentit.user.controller;

import com.rentit.user.api.UserRatingCreateRequest;
import com.rentit.user.service.UserRatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-ratings")
public class UserRatingController {

  private final UserRatingService userRatingService;

  public UserRatingController(UserRatingService userRatingService) {
    this.userRatingService = userRatingService;
  }

  @PostMapping
  public ResponseEntity create(@RequestBody UserRatingCreateRequest request) {
    userRatingService.create(request);
    return ResponseEntity.ok().build();
  }
}

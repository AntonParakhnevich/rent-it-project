package com.rentit.internal.notification.controller;

import com.rentit.internal.notification.api.InternalNotificationResponse;
import com.rentit.internal.notification.service.InternalNotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("internal-notifications")
public class InternalNotificationController {

  private final InternalNotificationService internalNotificationService;

  public InternalNotificationController(InternalNotificationService internalNotificationService) {
    this.internalNotificationService = internalNotificationService;
  }

  @GetMapping
  public Page<InternalNotificationResponse> getInternalNotificationsByUserId(@RequestParam("userId") Long userId,
      Pageable pageable) {
    return internalNotificationService.getInternalNotificationsByUserId(userId, pageable);
  }
}

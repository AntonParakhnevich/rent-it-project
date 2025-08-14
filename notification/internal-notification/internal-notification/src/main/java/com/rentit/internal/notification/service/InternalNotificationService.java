package com.rentit.internal.notification.service;

import com.rentit.internal.notification.api.InternalNotificationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InternalNotificationService {
  Page<InternalNotificationResponse> getInternalNotificationsByUserId(Long userId, Pageable pageable);
}

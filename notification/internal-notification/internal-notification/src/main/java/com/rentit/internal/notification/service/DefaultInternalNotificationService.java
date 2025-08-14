package com.rentit.internal.notification.service;

import com.rentit.internal.notification.api.InternalNotificationResponse;
import com.rentit.internal.notification.model.InternalNotification;
import com.rentit.internal.notification.repository.InternalNotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DefaultInternalNotificationService implements InternalNotificationService {

  private final InternalNotificationRepository internalNotificationRepository;

  public DefaultInternalNotificationService(InternalNotificationRepository internalNotificationRepository) {
    this.internalNotificationRepository = internalNotificationRepository;
  }

  public void create() {

  }

  public Page<InternalNotificationResponse> getInternalNotificationsByUserId(Long userId, Pageable pageable) {
    return internalNotificationRepository.findByUserId(userId, pageable)
        .map(this::convertToDto);
  }

  private InternalNotificationResponse convertToDto(InternalNotification internalNotification) {
    InternalNotificationResponse response = new InternalNotificationResponse();
    response.setId(internalNotification.getId());
    response.setUserId(internalNotification.getUserId());
    response.setMessage(internalNotification.getMessage());
    response.setDateCreated(internalNotification.getDateCreated());
    response.setViewed(internalNotification.getViewed());
    return response;
  }
}

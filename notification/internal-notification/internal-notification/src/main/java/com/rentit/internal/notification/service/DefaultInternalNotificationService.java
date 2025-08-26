package com.rentit.internal.notification.service;

import com.rentit.internal.notification.api.InternalNotificationResponse;
import com.rentit.internal.notification.dto.InternalNotificationModel;
import com.rentit.internal.notification.model.InternalNotification;
import com.rentit.internal.notification.repository.InternalNotificationRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefaultInternalNotificationService implements InternalNotificationService {

  private final InternalNotificationRepository internalNotificationRepository;

  public DefaultInternalNotificationService(InternalNotificationRepository internalNotificationRepository) {
    this.internalNotificationRepository = internalNotificationRepository;
  }

  @Override
  public void create(InternalNotificationModel model) {
    InternalNotification internalNotification = new InternalNotification();
    internalNotification.setType(model.getType());
    internalNotification.setDateCreated(model.getDateCreated());
    internalNotification.setViewed(model.getViewed());
    internalNotification.setMessage(model.getMessage());
    internalNotification.setUserId(model.getUserId());
    internalNotificationRepository.save(internalNotification);
  }

  @Override
  @Transactional
  public void createList(List<InternalNotificationModel> models) {
    List<InternalNotification> pojos = models.stream()
        .map(model -> {
          InternalNotification internalNotification = new InternalNotification();
          internalNotification.setType(model.getType());
          internalNotification.setDateCreated(model.getDateCreated());
          internalNotification.setViewed(model.getViewed());
          internalNotification.setMessage(model.getMessage());
          internalNotification.setUserId(model.getUserId());
          return internalNotification;
        })
        .collect(Collectors.toList());
    internalNotificationRepository.saveAll(pojos);
  }

  @Override
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

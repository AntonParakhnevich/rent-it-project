package com.rentit.internal.notification.dto;

import com.rentit.internal.notification.model.NotificationType;
import java.time.LocalDateTime;

public class InternalNotificationModel {

  private Long id;
  private Long userId;
  private String message;
  private LocalDateTime dateCreated;
  private Boolean isViewed;
  private NotificationType type;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public LocalDateTime getDateCreated() {
    return dateCreated;
  }

  public void setDateCreated(LocalDateTime dateCreated) {
    this.dateCreated = dateCreated;
  }

  public Boolean getViewed() {
    return isViewed;
  }

  public void setViewed(Boolean viewed) {
    isViewed = viewed;
  }

  public NotificationType getType() {
    return type;
  }

  public void setType(NotificationType type) {
    this.type = type;
  }
}

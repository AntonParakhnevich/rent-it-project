package com.rentit.internal.notification.api;

import java.time.LocalDateTime;

public class InternalNotificationResponse {

  private Long id;
  private Long userId;
  private String message;
  private LocalDateTime dateCreated;
  private Boolean isViewed;

  public InternalNotificationResponse() {
  }

  public InternalNotificationResponse(Long id, Long userId, String message, LocalDateTime dateCreated,
      Boolean isViewed) {
    this.id = id;
    this.userId = userId;
    this.message = message;
    this.dateCreated = dateCreated;
    this.isViewed = isViewed;
  }

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
}

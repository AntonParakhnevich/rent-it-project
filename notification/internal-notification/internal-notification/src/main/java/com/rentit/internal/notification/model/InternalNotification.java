package com.rentit.internal.notification.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "internal_notification")
public class InternalNotification {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "user_id")
  private Long userId;
  @Column(name = "messsage")
  private String message;
  @Column(name = "date_created")
  private LocalDateTime dateCreated;
  @Column(name = "is_viewed")
  private Boolean isViewed;

  @PrePersist
  protected void onCreate() {
    dateCreated = LocalDateTime.now();
  }


  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InternalNotification that = (InternalNotification) o;
    return Objects.equals(id, that.id) && Objects.equals(userId, that.userId)
        && Objects.equals(message, that.message) && Objects.equals(dateCreated, that.dateCreated)
        && Objects.equals(isViewed, that.isViewed);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, userId, message, dateCreated, isViewed);
  }

  @Override
  public String toString() {
    return "InternalNotification{" +
        "id=" + id +
        ", userId=" + userId +
        ", message='" + message + '\'' +
        ", dateCreated=" + dateCreated +
        ", isViewed=" + isViewed +
        '}';
  }
}

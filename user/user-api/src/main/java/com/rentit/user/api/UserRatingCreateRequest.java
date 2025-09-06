package com.rentit.user.api;

public class UserRatingCreateRequest {

  private Long userId;
  private Long reviewerId;
  private Integer rating;

  public UserRatingCreateRequest(Long userId, Long reviewerId, Integer rating) {
    this.userId = userId;
    this.reviewerId = reviewerId;
    this.rating = rating;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getReviewerId() {
    return reviewerId;
  }

  public void setReviewerId(Long reviewerId) {
    this.reviewerId = reviewerId;
  }

  public Integer getRating() {
    return rating;
  }

  public void setRating(Integer rating) {
    this.rating = rating;
  }
}

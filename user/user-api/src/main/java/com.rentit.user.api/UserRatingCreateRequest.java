package com.rentit.user.api;

public class UserRatingCreateRequest {

  private Long raterId;
  private Long ratedId;
  private Integer rating;

  public UserRatingCreateRequest(Long raterId, Long ratedId, Integer rating) {
    this.raterId = raterId;
    this.ratedId = ratedId;
    this.rating = rating;
  }

  public Long getRaterId() {
    return raterId;
  }

  public void setRaterId(Long raterId) {
    this.raterId = raterId;
  }

  public Long getRatedId() {
    return ratedId;
  }

  public void setRatedId(Long ratedId) {
    this.ratedId = ratedId;
  }

  public Integer getRating() {
    return rating;
  }

  public void setRating(Integer rating) {
    this.rating = rating;
  }
}

package com.rentit.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.Data;

@Data
@Entity
@Table(name = "user_rating")
public class UserRating {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private Integer rating;

  @Column(name = "rater_id")
  private Long raterId;

  @Column(name = "rated_id")
  private Long ratedId;

  @Column(name = "date")
  private LocalDateTime date;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserRating that = (UserRating) o;
    return Objects.equals(id, that.id) && Objects.equals(rating, that.rating)
        && Objects.equals(raterId, that.raterId) && Objects.equals(ratedId, that.ratedId)
        && Objects.equals(date, that.date);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, rating, raterId, ratedId, date);
  }

  @Override
  public String toString() {
    return "UserRating{" +
        "id=" + id +
        ", rating=" + rating +
        ", raterId=" + raterId +
        ", ratedId=" + ratedId +
        ", date=" + date +
        '}';
  }
}

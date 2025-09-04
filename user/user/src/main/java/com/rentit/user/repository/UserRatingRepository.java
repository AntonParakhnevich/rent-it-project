package com.rentit.user.repository;

import com.rentit.user.model.UserRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRatingRepository extends JpaRepository<UserRating, Long> {

  @Query("SELECT avg(ur.rating) FROM UserRating ur WHERE ur.userId = :userId")
  Double getAverageByUserId(Long userId);
}

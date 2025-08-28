package com.rentit.user.service;

import com.rentit.user.api.UserRatingCreateRequest;
import com.rentit.user.model.UserRating;
import com.rentit.user.repository.UserRatingRepository;
import org.springframework.stereotype.Service;

@Service
public class UserRatingService {

  private final UserRatingRepository userRatingRepository;

  public UserRatingService(UserRatingRepository userRatingRepository) {
    this.userRatingRepository = userRatingRepository;
  }

  public void create(UserRatingCreateRequest request) {
    UserRating userRating = new UserRating();
    userRating.setRating(request.getRating());
    userRating.setRatedId(request.getRatedId());
    userRating.setRaterId(request.getRaterId());
    userRatingRepository.save(userRating);
  }
}

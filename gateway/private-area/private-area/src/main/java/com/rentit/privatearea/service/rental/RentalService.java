package com.rentit.privatearea.service.rental;

import com.rentit.rental.api.RentalConnector;
import com.rentit.rental.api.RentalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RentalService {

  private final RentalConnector rentalConnector;

  public RentalResponse getById(Long id) {
    return rentalConnector.getById(id);
  }

  public void activate(Long id) {

  }

}

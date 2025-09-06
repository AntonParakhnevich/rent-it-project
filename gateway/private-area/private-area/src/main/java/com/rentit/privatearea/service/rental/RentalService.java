package com.rentit.privatearea.service.rental;

import com.rentit.rental.api.RentalConnector;
import com.rentit.rental.api.RentalResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RentalService {

  private final RentalConnector rentalConnector;

  public RentalResponse getById(Long id) {
    return rentalConnector.getById(id);
  }

  public Page<RentalResponse> getAllByUserId(Long userId, Pageable pageable) {
    return rentalConnector.getAllByUserId(userId,pageable);
  }

  public Page<RentalResponse> getAllByRenterId(Long renterId, Pageable pageable) {
    return rentalConnector.getAllByRenterId(renterId, pageable);
  }

  public Page<RentalResponse> getAllByOwnerId(Long ownerId, Pageable pageable) {
    return rentalConnector.getAllByOwnerId(ownerId, pageable);
  }

  public void confirm(Long id) {
    rentalConnector.confirm(id);
  }
}

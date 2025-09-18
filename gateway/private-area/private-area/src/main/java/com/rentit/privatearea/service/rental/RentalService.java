package com.rentit.privatearea.service.rental;

import com.rentit.privatearea.security.SessionService;
import com.rentit.rental.api.RentalConnector;
import com.rentit.rental.api.RentalRequest;
import com.rentit.rental.api.RentalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RentalService {

  private final RentalConnector rentalConnector;
  private final SessionService sessionService;

  public RentalResponse getById(Long id) {
    return rentalConnector.getById(id);
  }

  public Page<RentalResponse> getAllByUserId(Long userId, Pageable pageable) {
    if (!sessionService.getCurrentUserId().equals(userId)) {
      return null;
    }
    return rentalConnector.getAllByUserId(userId, pageable);
  }

  public Page<RentalResponse> getAllByRenterId(Long renterId, Pageable pageable) {
    if (!sessionService.getCurrentUserId().equals(renterId)) {
      return null;
    }
    return rentalConnector.getAllByRenterId(renterId, pageable);
  }

  public Page<RentalResponse> getAllByOwnerId(Long ownerId, Pageable pageable) {
    if (!sessionService.getCurrentUserId().equals(ownerId)) {
      return null;
    }
    return rentalConnector.getAllByOwnerId(ownerId, pageable);
  }

  public void confirm(Long id) {
    rentalConnector.confirm(id);
  }

  public RentalResponse createRental(RentalRequest request) {
    request.setRenterId(sessionService.getCurrentUserId());
    return rentalConnector.createRental(request);
  }

  public RentalResponse cancelRental(Long id) {
    RentalResponse rental = rentalConnector.getById(id);
    Long currentUserId = sessionService.getCurrentUserId();
    if (rental.getRenterId().equals(currentUserId) || rental.getLandLordId().equals(currentUserId)) {
      return rentalConnector.cancelRental(id);
    } else {
      throw new RuntimeException("У вас нет прав на отмену этой аренды");
    }
  }
}

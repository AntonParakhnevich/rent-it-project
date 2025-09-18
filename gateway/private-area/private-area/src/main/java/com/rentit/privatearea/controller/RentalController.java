package com.rentit.privatearea.controller;

import com.rentit.privatearea.service.rental.RentalService;
import com.rentit.rental.api.RentalRequest;
import com.rentit.rental.api.RentalResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/rentals")
public class RentalController {

  private final RentalService rentalService;

  @GetMapping("/{id}")
  public RentalResponse getById(@PathVariable("id") Long id) {
    return rentalService.getById(id);
  }

  @GetMapping
  public Page<RentalResponse> getAllByUserId(@RequestParam("userId") Long userId,  Pageable pageable) {
    return rentalService.getAllByUserId(userId, pageable);
  }

  @GetMapping("/renter/{renterId}")
  public Page<RentalResponse> getAllByRenterId(@PathVariable("renterId") Long renterId, Pageable pageable) {
    return rentalService.getAllByRenterId(renterId, pageable);
  }

  @GetMapping("/owner/{ownerId}")
  public Page<RentalResponse> getAllByOwnerId(@PathVariable("ownerId") Long ownerId, Pageable pageable) {
    return rentalService.getAllByOwnerId(ownerId, pageable);
  }

  @PostMapping("/confirm")
  public ResponseEntity<Void> confirm(@RequestParam("id") Long id) {
    rentalService.confirm(id);
    return ResponseEntity.ok().build();
  }

  @PostMapping
  public ResponseEntity<RentalResponse> createRental(@Valid @RequestBody RentalRequest request) {
    RentalResponse rental = rentalService.createRental(request);
    return ResponseEntity.ok(rental);
  }
}

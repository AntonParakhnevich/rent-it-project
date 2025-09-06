package com.rentit.controller;

import com.rentit.model.RentalStatus;
import com.rentit.rental.api.RentalRequest;
import com.rentit.rental.api.RentalResponse;
import com.rentit.service.RentalService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rentals")
@RequiredArgsConstructor
public class RentalController {

  private final RentalService rentalService;

  @PostMapping
  public ResponseEntity<RentalResponse> createRental(@Valid @RequestBody RentalRequest request) {
    return ResponseEntity.ok(rentalService.createRental(request));
  }

  @GetMapping("/{id}")
  public ResponseEntity<RentalResponse> getRental(@PathVariable("id") Long id) {
    return ResponseEntity.ok(rentalService.getRentalById(id));
  }

  @GetMapping
  public Page<RentalResponse> getRentalsByUserId(@RequestParam("userId") Long userId, Pageable pageable) {
    return rentalService.getRentalsByUserId(userId, pageable);
  }

  @PostMapping("/confirm")
  public void updateRentalStatus(@RequestParam("id") Long id) {
    rentalService.confirm(id);
  }

  @PutMapping("/complete/{id}")
  public ResponseEntity<RentalResponse> complete(@PathVariable Long id) {
    return ResponseEntity.ok(rentalService.updateRentalStatus(id, RentalStatus.COMPLETED));
  }

  @GetMapping("/renter/{renterId}")
  public ResponseEntity<Page<RentalResponse>> getRentalsByRenter(@PathVariable Long renterId, Pageable pageable) {
    return ResponseEntity.ok(rentalService.getRentalsByRenter(renterId, pageable));
  }

  @GetMapping("/owner/{ownerId}")
  public ResponseEntity<Page<RentalResponse>> getRentalsByOwner(@PathVariable Long ownerId, Pageable pageable) {
    return ResponseEntity.ok(rentalService.getRentalsByOwner(ownerId, pageable));
  }
} 
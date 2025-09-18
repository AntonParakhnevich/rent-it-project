package com.rentit.rental.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "rentalConnector", url = "http://localhost:8081")
public interface RentalConnector {

  @GetMapping("/rentals/{id}")
  RentalResponse getById(@PathVariable("id") Long id);

  @GetMapping("/rentals?userId={userId}")
  Page<RentalResponse> getAllByUserId(@PathVariable("userId") Long userId, Pageable pageable);

  @GetMapping("/rentals/renter/{renterId}")
  Page<RentalResponse> getAllByRenterId(@PathVariable("renterId") Long renterId, Pageable pageable);

  @GetMapping("/rentals/owner/{ownerId}")
  Page<RentalResponse> getAllByOwnerId(@PathVariable("ownerId") Long ownerId, Pageable pageable);

  @PostMapping("/rentals/confirm?id={id}")
  RentalResponse confirm(@PathVariable("id") Long id);

  @PostMapping("/rentals")
  RentalResponse createRental(@RequestBody RentalRequest request);

  @PutMapping("/rentals/cancel/{id}")
  RentalResponse cancelRental(@PathVariable("id") Long id);
}

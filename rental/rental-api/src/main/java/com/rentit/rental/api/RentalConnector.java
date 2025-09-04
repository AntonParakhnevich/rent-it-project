package com.rentit.rental.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "rentalConnector", url = "http://localhost:8081")
public interface RentalConnector {

  @GetMapping("/rentals/{id}")
  RentalResponse getById(@PathVariable("id") Long id);
}

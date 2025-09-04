package com.rentit.rental.api;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "rentalConnector", url = "http://localhost:8081")
public interface RentalConnector {

  @GetMapping("/rentals/{id}")
  RentalResponse getById(@PathVariable("id") Long id);

  @GetMapping("/rentals?userId={userId}")
  List<RentalResponse> getAllByUserId(@PathVariable("userId") Long userId);

  @PostMapping("/rentals/confirm?id={id}")
  void confirm(@PathVariable("id") Long id);
}

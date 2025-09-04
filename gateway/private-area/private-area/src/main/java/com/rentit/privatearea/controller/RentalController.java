package com.rentit.privatearea.controller;

import com.rentit.privatearea.service.rental.RentalService;
import com.rentit.rental.api.RentalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

  @PostMapping("/activate")
  public ResponseEntity activate(@RequestParam("id") Long id) {
    rentalService.activate(id);
    return ResponseEntity.ok().build();
  }
}

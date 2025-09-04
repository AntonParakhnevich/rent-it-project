package com.rentit.privatearea.controller;

import com.rentit.privatearea.service.rental.RentalService;
import com.rentit.rental.api.RentalResponse;
import java.util.List;
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

  @GetMapping
  public List<RentalResponse> getAllByUserId(@RequestParam("userId") Long userId) {
    return rentalService.getAllByUserId(userId);
  }

  @PostMapping("/confirm")
  public ResponseEntity<Void> confirm(@RequestParam("id") Long id) {
    rentalService.confirm(id);
    return ResponseEntity.ok().build();
  }
}

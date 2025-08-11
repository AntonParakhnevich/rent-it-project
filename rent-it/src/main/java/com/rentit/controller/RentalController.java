package com.rentit.controller;

import com.rentit.dto.RentalDto;
import com.rentit.model.RentalStatus;
import com.rentit.service.RentalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
public class RentalController {
    private final RentalService rentalService;

    @PostMapping
    public ResponseEntity<RentalDto> createRental(@Valid @RequestBody RentalDto rentalDto) {
        return ResponseEntity.ok(rentalService.createRental(rentalDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalDto> getRental(@PathVariable Long id) {
        return ResponseEntity.ok(rentalService.getRentalById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<RentalDto> updateRentalStatus(
            @PathVariable Long id,
            @RequestParam RentalStatus status) {
        return ResponseEntity.ok(rentalService.updateRentalStatus(id, status));
    }

    @GetMapping("/renter/{renterId}")
    public ResponseEntity<Page<RentalDto>> getRentalsByRenter(
            @PathVariable Long renterId,
            Pageable pageable) {
        return ResponseEntity.ok(rentalService.getRentalsByRenter(renterId, pageable));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<Page<RentalDto>> getRentalsByOwner(
            @PathVariable Long ownerId,
            Pageable pageable) {
        return ResponseEntity.ok(rentalService.getRentalsByOwner(ownerId, pageable));
    }
} 
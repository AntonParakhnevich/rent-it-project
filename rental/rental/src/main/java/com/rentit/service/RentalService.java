package com.rentit.service;

import com.rentit.model.Item;
import com.rentit.model.Rental;
import com.rentit.model.RentalStatus;
import com.rentit.rental.api.RentalRequest;
import com.rentit.rental.api.RentalResponse;
import com.rentit.repository.ItemRepository;
import com.rentit.repository.RentalRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RentalService {

  private final RentalRepository rentalRepository;
  private final ItemRepository itemRepository;

  @Transactional
  public RentalResponse createRental(RentalRequest request) {
    Item item = itemRepository.findById(request.getItemId())
        .orElseThrow(() -> new RuntimeException("Предмет не найден"));

    // Проверка доступности предмета на указанные даты
    List<Rental> overlappingRentals = rentalRepository.findOverlappingRentals(
        item.getId(), request.getStartDate(), request.getEndDate());

    if (!overlappingRentals.isEmpty()) {
      throw new RuntimeException("Предмет уже забронирован на указанные даты");
    }

    Rental rental = new Rental();
    rental.setItem(item);
    rental.setUserId(request.getRenterId());
    rental.setStartDate(request.getStartDate());
    rental.setEndDate(request.getEndDate());
    rental.setTotalPrice(calculateTotalPrice(request.getStartDate(), request.getEndDate(), item.getPricePerDay()));
    rental.setDepositAmount(request.getDepositAmount());
    rental.setStatus(RentalStatus.PENDING);

    Rental savedRental = rentalRepository.save(rental);
    return mapToResponse(savedRental);
  }

  @Transactional(readOnly = true)
  public RentalResponse getRentalById(Long id) {
    Rental rental = rentalRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Аренда не найдена"));
    return mapToResponse(rental);
  }

  @Transactional(readOnly = true)
  public List<RentalResponse> getRentalsByUserId(Long userId) {
    List<Rental> rentals = rentalRepository.findByUserId(userId);
    return rentals.stream()
        .map(this::mapToResponse)
        .collect(Collectors.toList());
  }

  @Transactional
  public void confirm(Long rentalId) {
    Rental rental = rentalRepository.findById(rentalId)
        .orElseThrow(() -> new IllegalArgumentException("rental not found, id=" + rentalId));
    if (rental.getStatus() == RentalStatus.PENDING) {
      rental.setStatus(RentalStatus.CONFIRMED);
      rentalRepository.save(rental);
    }
  }

  @Transactional
  public RentalResponse updateRentalStatus(Long id, RentalStatus status) {
    Rental rental = rentalRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Аренда не найдена"));

    rental.setStatus(status);
    Rental updatedRental = rentalRepository.save(rental);
    return mapToResponse(updatedRental);
  }

  @Transactional(readOnly = true)
  public Page<RentalResponse> getRentalsByRenter(Long renterId, Pageable pageable) {
    return rentalRepository.findByUserId(renterId, pageable)
        .map(this::mapToResponse);
  }

  @Transactional(readOnly = true)
  public Page<RentalResponse> getRentalsByOwner(Long ownerId, Pageable pageable) {
    return rentalRepository.findByItemUserId(ownerId, pageable)
        .map(this::mapToResponse);
  }

  private BigDecimal calculateTotalPrice(LocalDateTime startDate, LocalDateTime endDate, BigDecimal pricePerDay) {
    long days = ChronoUnit.DAYS.between(startDate, endDate);
    return pricePerDay.multiply(new BigDecimal(days));
  }

  private RentalResponse mapToResponse(Rental rental) {
    RentalResponse rentalResponse = new RentalResponse();
    rentalResponse.setId(rental.getId());
    rentalResponse.setItemId(rental.getItem().getId());
    rentalResponse.setDepositAmount(rental.getDepositAmount());
    rentalResponse.setStatus(rental.getStatus().name());
    rentalResponse.setEndDate(rental.getEndDate());
    rentalResponse.setStartDate(rental.getStartDate());
    rentalResponse.setTotalPrice(rental.getTotalPrice());

    return rentalResponse;
  }
} 
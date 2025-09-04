package com.rentit.service;

import com.rentit.dto.RentalDto;
import com.rentit.model.Item;
import com.rentit.model.Rental;
import com.rentit.model.RentalStatus;
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
  public RentalDto createRental(RentalDto rentalDto) {
    Item item = itemRepository.findById(rentalDto.getItemId())
        .orElseThrow(() -> new RuntimeException("Предмет не найден"));

    // Проверка доступности предмета на указанные даты
    List<Rental> overlappingRentals = rentalRepository.findOverlappingRentals(
        item.getId(), rentalDto.getStartDate(), rentalDto.getEndDate());

    if (!overlappingRentals.isEmpty()) {
      throw new RuntimeException("Предмет уже забронирован на указанные даты");
    }

    Rental rental = new Rental();
    rental.setItem(item);
    rental.setUserId(rentalDto.getRenterId());
    rental.setStartDate(rentalDto.getStartDate());
    rental.setEndDate(rentalDto.getEndDate());
    rental.setTotalPrice(calculateTotalPrice(rentalDto.getStartDate(), rentalDto.getEndDate(), item.getPricePerDay()));
    rental.setDepositAmount(rentalDto.getDepositAmount());
    rental.setStatus(RentalStatus.PENDING);

    Rental savedRental = rentalRepository.save(rental);
    return convertToDto(savedRental);
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
  public RentalDto updateRentalStatus(Long id, RentalStatus status) {
    Rental rental = rentalRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Аренда не найдена"));

    rental.setStatus(status);
    Rental updatedRental = rentalRepository.save(rental);
    return convertToDto(updatedRental);
  }

  @Transactional(readOnly = true)
  public Page<RentalDto> getRentalsByRenter(Long renterId, Pageable pageable) {
    return rentalRepository.findByUserId(renterId, pageable)
        .map(this::convertToDto);
  }

  @Transactional(readOnly = true)
  public Page<RentalDto> getRentalsByOwner(Long ownerId, Pageable pageable) {
    return rentalRepository.findByItemUserId(ownerId, pageable)
        .map(this::convertToDto);
  }

  private RentalDto convertToDto(Rental rental) {
    RentalDto dto = new RentalDto();
    dto.setId(rental.getId());
    dto.setItemId(rental.getItem().getId());
    dto.setRenterId(rental.getUserId());
    dto.setStartDate(rental.getStartDate());
    dto.setEndDate(rental.getEndDate());
    dto.setTotalPrice(rental.getTotalPrice());
    dto.setDepositAmount(rental.getDepositAmount());
    dto.setStatus(rental.getStatus());
    return dto;
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
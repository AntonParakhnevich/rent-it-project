package com.rentit.service;

import com.rentit.model.Item;
import com.rentit.model.ItemCategory;
import com.rentit.model.RentalStatus;
import com.rentit.rental.api.ItemRequest;
import com.rentit.rental.api.ItemResponse;
import com.rentit.rental.api.UnavailableDateItemResponse;
import com.rentit.repository.ItemRepository;
import com.rentit.repository.RentalRepository;
import com.rentit.user.api.UserConnector;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemService {

  private final ItemRepository itemRepository;
  private final RentalRepository rentalRepository;
  private final UserConnector userConnector;

  private final List<RentalStatus> ACTIVE_STATUSES = Arrays.asList(RentalStatus.PENDING, RentalStatus.CONFIRMED,
      RentalStatus.IN_PROGRESS);

  @Transactional
  public ItemResponse createItem(ItemRequest request) {
    boolean isExistOwner = Optional.ofNullable(userConnector.getUserById(request.getOwnerId()))
        .isPresent();

    if (!isExistOwner) {
      return null;
    }

    Item item = new Item();
    item.setTitle(request.getTitle());
    item.setDescription(request.getDescription());
    item.setPricePerDay(request.getPricePerDay());
    item.setDepositAmount(request.getDepositAmount());
    item.setCategory(ItemCategory.valueOf(request.getCategory()));
    item.setImages(request.getImages());
    item.setAvailable(request.isAvailable());
    item.setLocation(request.getLocation());
    item.setUserId(request.getOwnerId());

    item = itemRepository.save(item);
    return mapToResponse(item);
  }

  @Transactional(readOnly = true)
  public ItemResponse getItemById(Long id) {
    Item item = itemRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Предмет не найден"));
    return mapToResponse(item);
  }

  @Transactional
  public ItemResponse updateItem(Long id, ItemRequest request) {
    Item item = itemRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Предмет не найден"));

    item.setTitle(request.getTitle());
    item.setDescription(request.getDescription());
    item.setPricePerDay(request.getPricePerDay());
    item.setDepositAmount(request.getDepositAmount());
    item.setCategory(ItemCategory.valueOf(request.getCategory()));
    item.setImages(request.getImages());
    item.setAvailable(request.isAvailable());
    item.setLocation(request.getLocation());

    Item updatedItem = itemRepository.save(item);
    return mapToResponse(updatedItem);
  }

  @Transactional
  public void deleteItem(Long id) {
    if (!itemRepository.existsById(id)) {
      throw new RuntimeException("Предмет не найден");
    }
    itemRepository.deleteById(id);
  }

  @Transactional(readOnly = true)
  public Page<ItemResponse> getItemsByCategory(ItemCategory category, Pageable pageable) {
    return itemRepository.findByCategory(category, pageable)
        .map(this::mapToResponse);
  }

  @Transactional(readOnly = true)
  public Page<ItemResponse> getItemsByOwner(Long ownerId, Pageable pageable) {
    return itemRepository.findByUserId(ownerId, pageable)
        .map(this::mapToResponse);
  }

  @Transactional(readOnly = true)
  public Page<ItemResponse> getItemsByMaxPrice(BigDecimal maxPrice, Pageable pageable) {
    return itemRepository.findByMaxPrice(maxPrice, pageable)
        .map(this::mapToResponse);
  }

  @Transactional(readOnly = true)
  public List<ItemResponse> getItemsByLocation(String location) {
    return itemRepository.findByLocationContainingIgnoreCase(location)
        .stream()
        .map(this::mapToResponse)
        .toList();
  }

  @Transactional(readOnly = true)
  public Page<ItemResponse> getItemsByLocationPaged(String location, Pageable pageable) {
    return itemRepository.findByLocationContainingIgnoreCase(location, pageable)
        .map(this::mapToResponse);
  }

  @Transactional(readOnly = true)
  public Page<ItemResponse> getAvailableItems(Pageable pageable) {
    return itemRepository.findByIsAvailable(true, pageable)
        .map(this::mapToResponse);
  }

  @Transactional(readOnly = true)
  public Page<ItemResponse> searchItems(ItemCategory category, BigDecimal maxPrice, String location, Pageable pageable) {
    return itemRepository.findByFilters(category, maxPrice, location, pageable)
        .map(this::mapToResponse);
  }

  public UnavailableDateItemResponse getUnavailableDatesByItemIdBetweenDates(Long itemId, LocalDate startDate,
      LocalDate endDate) {
    Set<LocalDate> unavailableDates = rentalRepository.findByItemIdAndStatusInAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByStartDateAsc(
            itemId,
            ACTIVE_STATUSES,
            LocalDateTime.of(endDate, LocalTime.MAX),
            LocalDateTime.of(startDate, LocalDateTime.MIN.toLocalTime()))
        .stream()
        .flatMap(rental -> intersect(startDate, endDate, rental.getStartDate().toLocalDate(),
            rental.getEndDate().toLocalDate()).stream())
        .collect(Collectors.toSet());

    return new UnavailableDateItemResponse(itemId, unavailableDates);
  }

  private Set<LocalDate> intersect(LocalDate firstStartDate, LocalDate firstEndDate, LocalDate secondStartDate,
      LocalDate secondEndDate) {
    LocalDate maxStart =
        firstStartDate.isAfter(secondStartDate) || firstStartDate.isEqual(secondStartDate) ? firstStartDate
            : secondStartDate;
    LocalDate minEnd =
        firstEndDate.isBefore(secondEndDate) || firstEndDate.isEqual(secondEndDate) ? firstEndDate : secondEndDate;

    if (maxStart.isAfter(minEnd)) {
      return Set.of();
    }

    long days = java.time.temporal.ChronoUnit.DAYS.between(maxStart, minEnd);
    return Stream.iterate(maxStart, d -> d.plusDays(1))
        .limit(days + 1) // +1, чтобы включить minEnd
        .collect(Collectors.toSet());
  }

  private ItemResponse mapToResponse(Item item) {
    ItemResponse dto = new ItemResponse();
    dto.setId(item.getId());
    dto.setTitle(item.getTitle());
    dto.setDescription(item.getDescription());
    dto.setPricePerDay(item.getPricePerDay());
    dto.setDepositAmount(item.getDepositAmount());
    dto.setCategory(item.getCategory().name());
    dto.setImages(item.getImages());
    dto.setAvailable(item.isAvailable());
    dto.setLocation(item.getLocation());
    dto.setOwnerId(item.getUserId());
    return dto;
  }
} 
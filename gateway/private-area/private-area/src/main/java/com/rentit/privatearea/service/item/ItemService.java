package com.rentit.privatearea.service.item;

import com.rentit.privatearea.security.SessionService;
import com.rentit.rental.api.ItemConnector;
import com.rentit.rental.api.ItemRequest;
import com.rentit.rental.api.ItemResponse;
import com.rentit.rental.api.UnavailableDateItemResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

  private final ItemConnector itemConnector;
  private final SessionService sessionService;

  public ItemResponse create(ItemRequest request) {
    if (!sessionService.getCurrentUserId().equals(request.getOwnerId())) {
      return null;
    }
    return itemConnector.create(request);
  }

  public ItemResponse getById(Long id) {
    return itemConnector.getById(id);
  }

  public Page<ItemResponse> getByOwnerId(Long ownerId, Pageable pageable) {
    return itemConnector.getByOwnerId(ownerId, pageable);
  }

  public Page<ItemResponse> getAvailableItems(Pageable pageable) {
    return itemConnector.getAvailableItems(true, pageable);
  }

  public Page<ItemResponse> searchItems(String category, BigDecimal maxPrice, String location, Pageable pageable) {
    return itemConnector.searchItems(category, maxPrice, location, true, pageable);
  }

  public Page<ItemResponse> getByCategory(String category, Pageable pageable) {
    return itemConnector.getByCategory(category, pageable);
  }

  public Page<ItemResponse> getByMaxPrice(BigDecimal maxPrice, Pageable pageable) {
    return itemConnector.getByMaxPrice(maxPrice, pageable);
  }

  public Page<ItemResponse> getByLocation(String location, Pageable pageable) {
    return itemConnector.getByLocation(location, pageable);
  }

  public UnavailableDateItemResponse getUnavailableDatesByItemIdAndDates(Long itemId, LocalDate startDate,
      LocalDate endDate) {
    return itemConnector.getUnavailableDatesByItemIdAndDates(itemId, startDate, endDate);
  }
}

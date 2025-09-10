package com.rentit.privatearea.service.item;

import com.rentit.privatearea.security.SessionService;
import com.rentit.rental.api.ItemConnector;
import com.rentit.rental.api.ItemRequest;
import com.rentit.rental.api.ItemResponse;
import com.rentit.rental.api.UnavailableDateItemResponse;
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

  public Page<ItemResponse> getByOwnerId(Long ownerId, Pageable pageable) {
    return itemConnector.getByOwnerId(ownerId, pageable);
  }

  public UnavailableDateItemResponse getUnavailableDatesByItemIdAndDates(Long itemId, LocalDate startDate,
      LocalDate endDate) {
    return itemConnector.getUnavailableDatesByItemIdAndDates(itemId, startDate, endDate);
  }
}

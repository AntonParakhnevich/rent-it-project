package com.rentit.privatearea.controller;

import com.rentit.privatearea.service.item.ItemService;
import com.rentit.rental.api.ItemRequest;
import com.rentit.rental.api.ItemResponse;
import com.rentit.rental.api.UnavailableDateItemResponse;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/items")
public class ItemController {

  private final ItemService itemService;

  @PostMapping
  public ItemResponse create(@RequestBody ItemRequest request) {
    return itemService.create(request);
  }

  @GetMapping
  public Page<ItemResponse> getByOwnerId(@RequestParam("ownerId") Long ownerId, Pageable pageable) {
    return itemService.getByOwnerId(ownerId, pageable);
  }

  @GetMapping("/unavailable-dates")
  public UnavailableDateItemResponse getUnavailableDatesByItemIdAndDates(
      @RequestParam("itemId") Long itemId,
      @RequestParam("startDate") LocalDate startDate,
      @RequestParam("endDate") LocalDate endDate
  ) {
    return itemService.getUnavailableDatesByItemIdAndDates(itemId, startDate, endDate);
  }
}

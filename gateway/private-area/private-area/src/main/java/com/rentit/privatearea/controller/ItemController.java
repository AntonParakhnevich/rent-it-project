package com.rentit.privatearea.controller;

import com.rentit.privatearea.service.item.ItemService;
import com.rentit.rental.api.ItemRequest;
import com.rentit.rental.api.ItemResponse;
import com.rentit.rental.api.UnavailableDateItemResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  @GetMapping("/{id}")
  public ItemResponse getById(@PathVariable Long id) {
    return itemService.getById(id);
  }

  @GetMapping
  public Page<ItemResponse> getItems(
      @RequestParam(required = false) Long ownerId,
      @RequestParam(required = false) Boolean available,
      @RequestParam(required = false) String category,
      @RequestParam(required = false) BigDecimal maxPrice,
      @RequestParam(required = false) String location,
      Pageable pageable) {
    
    // Если указан владелец, возвращаем его товары
    if (ownerId != null) {
      return itemService.getByOwnerId(ownerId, pageable);
    }
    
    // Если указан флаг доступности
    if (available != null && available) {
      // Если есть дополнительные фильтры, используем комплексный поиск
      if (category != null || maxPrice != null || location != null) {
        return itemService.searchItems(category, maxPrice, location, pageable);
      }
      // Иначе просто доступные товары
      return itemService.getAvailableItems(pageable);
    }
    
    // Комплексный поиск с фильтрами
    if (category != null || maxPrice != null || location != null) {
      return itemService.searchItems(category, maxPrice, location, pageable);
    }
    
    // По умолчанию возвращаем доступные товары
    return itemService.getAvailableItems(pageable);
  }

  @GetMapping("/category/{category}")
  public Page<ItemResponse> getByCategory(@PathVariable String category, Pageable pageable) {
    return itemService.getByCategory(category, pageable);
  }

  @GetMapping("/price")
  public Page<ItemResponse> getByMaxPrice(@RequestParam BigDecimal maxPrice, Pageable pageable) {
    return itemService.getByMaxPrice(maxPrice, pageable);
  }

  @GetMapping("/location")
  public Page<ItemResponse> getByLocation(@RequestParam String location, Pageable pageable) {
    return itemService.getByLocation(location, pageable);
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

package com.rentit.controller;

import com.rentit.model.ItemCategory;
import com.rentit.rental.api.ItemRequest;
import com.rentit.rental.api.ItemResponse;
import com.rentit.rental.api.UnavailableDateItemResponse;
import com.rentit.service.ItemService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

  private final ItemService itemService;

  @PostMapping
  public ItemResponse createItem(@Valid @RequestBody ItemRequest request) {
    return itemService.createItem(request);
  }

  @GetMapping("/{id}")
  public ItemResponse getItem(@PathVariable Long id) {
    return itemService.getItemById(id);
  }

  @PutMapping("/{id}")
  public ItemResponse updateItem(@PathVariable Long id, @Valid @RequestBody ItemRequest request) {
    return itemService.updateItem(id, request);
  }

  @DeleteMapping("/{id}")
  public void deleteItem(@PathVariable Long id) {
    itemService.deleteItem(id);
  }

  @GetMapping("/category/{category}")
  public Page<ItemResponse> getItemsByCategory(@PathVariable ItemCategory category, Pageable pageable) {
    return itemService.getItemsByCategory(category, pageable);
  }

  @GetMapping
  public Page<ItemResponse> getItemsByOwner(@RequestParam Long ownerId, Pageable pageable) {
    return itemService.getItemsByOwner(ownerId, pageable);
  }

  @GetMapping("/price")
  public Page<ItemResponse> getItemsByMaxPrice(@RequestParam BigDecimal maxPrice, Pageable pageable) {
    return itemService.getItemsByMaxPrice(maxPrice, pageable);
  }

  @GetMapping("/location")
  public List<ItemResponse> getItemsByLocation(@RequestParam String location) {
    return itemService.getItemsByLocation(location);
  }

  @GetMapping("/unavailable-dates")
  public UnavailableDateItemResponse getUnavailableDateByItemId(
      @RequestParam("itemId") Long itemId,
      @RequestParam("startDate") LocalDate startDate,
      @RequestParam("endDate") LocalDate endDate) {
    return itemService.getUnavailableDatesByItemIdBetweenDates(itemId, startDate, endDate);

  }
}
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
  public Page<ItemResponse> getItems(
      @RequestParam(required = false) Long ownerId,
      @RequestParam(required = false) Boolean available,
      @RequestParam(required = false) String category,
      @RequestParam(required = false) BigDecimal maxPrice,
      @RequestParam(required = false) String location,
      Pageable pageable) {
    
    // Преобразуем строку категории в enum
    ItemCategory categoryEnum = null;
    if (category != null && !category.isEmpty()) {
      try {
        categoryEnum = ItemCategory.valueOf(category.toUpperCase());
        log.debug("Converted category '{}' to enum: {}", category, categoryEnum);
      } catch (IllegalArgumentException e) {
        // Если категория не найдена, игнорируем её
        log.warn("Invalid category '{}', ignoring", category);
        categoryEnum = null;
      }
    }
    
    log.debug("Search parameters: ownerId={}, available={}, category={}, categoryEnum={}, maxPrice={}, location={}", 
        ownerId, available, category, categoryEnum, maxPrice, location);
    
    // Если указан владелец, возвращаем его товары
    if (ownerId != null) {
      return itemService.getItemsByOwner(ownerId, pageable);
    }
    
    // Если указан флаг доступности
    if (available != null && available) {
      // Если есть дополнительные фильтры, используем комплексный поиск
      if (categoryEnum != null || maxPrice != null || location != null) {
        return itemService.searchItems(categoryEnum, maxPrice, location, pageable);
      }
      // Иначе просто доступные товары
      return itemService.getAvailableItems(pageable);
    }
    
    // Комплексный поиск с фильтрами
    if (categoryEnum != null || maxPrice != null || location != null) {
      return itemService.searchItems(categoryEnum, maxPrice, location, pageable);
    }
    
    // По умолчанию возвращаем доступные товары
    return itemService.getAvailableItems(pageable);
  }

  @GetMapping("/price")
  public Page<ItemResponse> getItemsByMaxPrice(@RequestParam BigDecimal maxPrice, Pageable pageable) {
    return itemService.getItemsByMaxPrice(maxPrice, pageable);
  }

  @GetMapping("/location")
  public Page<ItemResponse> getItemsByLocation(@RequestParam String location, Pageable pageable) {
    return itemService.getItemsByLocationPaged(location, pageable);
  }

  @GetMapping("/unavailable-dates")
  public UnavailableDateItemResponse getUnavailableDateByItemId(
      @RequestParam("itemId") Long itemId,
      @RequestParam("startDate") LocalDate startDate,
      @RequestParam("endDate") LocalDate endDate) {
    return itemService.getUnavailableDatesByItemIdBetweenDates(itemId, startDate, endDate);

  }
}
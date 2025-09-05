package com.rentit.controller;

import com.rentit.model.ItemCategory;
import com.rentit.rental.api.ItemRequest;
import com.rentit.rental.api.ItemResponse;
import com.rentit.service.ItemService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<ItemResponse> createItem(@Valid @RequestBody ItemRequest request) {
    return ResponseEntity.ok(itemService.createItem(request));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ItemResponse> getItem(@PathVariable Long id) {
    return ResponseEntity.ok(itemService.getItemById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ItemResponse> updateItem(@PathVariable Long id, @Valid @RequestBody ItemRequest request) {
    return ResponseEntity.ok(itemService.updateItem(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
    itemService.deleteItem(id);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/category/{category}")
  public ResponseEntity<Page<ItemResponse>> getItemsByCategory(@PathVariable ItemCategory category, Pageable pageable) {
    return ResponseEntity.ok(itemService.getItemsByCategory(category, pageable));
  }

  @GetMapping
  public ResponseEntity<Page<ItemResponse>> getItemsByOwner(@RequestParam Long ownerId, Pageable pageable) {
    return ResponseEntity.ok(itemService.getItemsByOwner(ownerId, pageable));
  }

  @GetMapping("/price")
  public ResponseEntity<Page<ItemResponse>> getItemsByMaxPrice(@RequestParam BigDecimal maxPrice, Pageable pageable) {
    return ResponseEntity.ok(itemService.getItemsByMaxPrice(maxPrice, pageable));
  }

  @GetMapping("/location")
  public ResponseEntity<List<ItemResponse>> getItemsByLocation(@RequestParam String location) {
    return ResponseEntity.ok(itemService.getItemsByLocation(location));
  }
} 
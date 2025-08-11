package com.rentit.controller;

import com.rentit.dto.ItemDto;
import com.rentit.model.ItemCategory;
import com.rentit.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDto> createItem(@Valid @RequestBody ItemDto itemDto) {
        return ResponseEntity.ok(itemService.createItem(itemDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItem(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.getItemById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemDto> updateItem(
            @PathVariable Long id,
            @Valid @RequestBody ItemDto itemDto) {
        return ResponseEntity.ok(itemService.updateItem(id, itemDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Page<ItemDto>> getItemsByCategory(
            @PathVariable ItemCategory category,
            Pageable pageable) {
        return ResponseEntity.ok(itemService.getItemsByCategory(category, pageable));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<Page<ItemDto>> getItemsByOwner(
            @PathVariable Long ownerId,
            Pageable pageable) {
        return ResponseEntity.ok(itemService.getItemsByOwner(ownerId, pageable));
    }

    @GetMapping("/price")
    public ResponseEntity<Page<ItemDto>> getItemsByMaxPrice(
            @RequestParam BigDecimal maxPrice,
            Pageable pageable) {
        return ResponseEntity.ok(itemService.getItemsByMaxPrice(maxPrice, pageable));
    }

    @GetMapping("/location")
    public ResponseEntity<List<ItemDto>> getItemsByLocation(@RequestParam String location) {
        return ResponseEntity.ok(itemService.getItemsByLocation(location));
    }
} 
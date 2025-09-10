package com.rentit.rental.api;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "itemConnector", url = "http://localhost:8081")
public interface ItemConnector {

  @GetMapping("/items/{id}")
  ItemResponse getById(@PathVariable("id") Long id);

  @PostMapping("/items")
  ItemResponse create(ItemRequest request);

  @GetMapping("/items")
  Page<ItemResponse> getByOwnerId(@RequestParam("ownerId") Long ownerId, Pageable pageable);

  @GetMapping("/items")
  Page<ItemResponse> getAvailableItems(@RequestParam("available") Boolean available, Pageable pageable);

  @GetMapping("/items")
  Page<ItemResponse> searchItems(
      @RequestParam(required = false) String category,
      @RequestParam(required = false) BigDecimal maxPrice,
      @RequestParam(required = false) String location,
      @RequestParam(required = false) Boolean available,
      Pageable pageable);

  @GetMapping("/items/category/{category}")
  Page<ItemResponse> getByCategory(@PathVariable("category") String category, Pageable pageable);

  @GetMapping("/items/price")
  Page<ItemResponse> getByMaxPrice(@RequestParam("maxPrice") BigDecimal maxPrice, Pageable pageable);

  @GetMapping("/items/location")
  Page<ItemResponse> getByLocation(@RequestParam("location") String location, Pageable pageable);

  @GetMapping("/items/unavailable-dates")
  UnavailableDateItemResponse getUnavailableDatesByItemIdAndDates(
      @RequestParam("itemId") Long itemId,
      @RequestParam("startDate") LocalDate startDate, 
      @RequestParam("endDate") LocalDate endDate);
}

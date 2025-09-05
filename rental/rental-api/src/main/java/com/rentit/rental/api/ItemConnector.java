package com.rentit.rental.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "itemConnector", url = "http://localhost:8081")
public interface ItemConnector {

  @GetMapping("/items/{id}")
  ResponseEntity<ItemResponse> getById(@PathVariable("id") Long id);

  @PostMapping("/items")
  ResponseEntity<ItemResponse> create(ItemRequest request);

  @GetMapping("/items?ownerId={ownerId}")
  ResponseEntity<Page<ItemResponse>> getByOwnerId(@PathVariable("ownerId") Long ownerId, Pageable pageable);
}

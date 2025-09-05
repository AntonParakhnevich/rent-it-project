package com.rentit.privatearea.service.item;

import com.rentit.rental.api.ItemConnector;
import com.rentit.rental.api.ItemRequest;
import com.rentit.rental.api.ItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

  private final ItemConnector itemConnector;

  public ResponseEntity<ItemResponse> create(ItemRequest request) {
    return itemConnector.create(request);
  }

  public ResponseEntity<Page<ItemResponse>> getByOwnerId(Long ownerId, Pageable pageable){
    return itemConnector.getByOwnerId(ownerId, pageable);
  }
}

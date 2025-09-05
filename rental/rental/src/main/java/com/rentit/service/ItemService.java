package com.rentit.service;

import com.rentit.model.Item;
import com.rentit.model.ItemCategory;
import com.rentit.rental.api.ItemRequest;
import com.rentit.rental.api.ItemResponse;
import com.rentit.repository.ItemRepository;
import com.rentit.user.api.UserConnector;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemService {

  private final ItemRepository itemRepository;
  private final UserConnector userConnector;

  @Transactional
  public ItemResponse createItem(ItemRequest request) {
    boolean isExistOwner = Optional.ofNullable(userConnector.getUserById(request.getOwnerId()))
        .isPresent();

    if (!isExistOwner) {
      return null;
    }

    Item item = new Item();
    item.setTitle(request.getTitle());
    item.setDescription(request.getDescription());
    item.setPricePerDay(request.getPricePerDay());
    item.setDepositAmount(request.getDepositAmount());
    item.setCategory(ItemCategory.valueOf(request.getCategory()));
    item.setImages(request.getImages());
    item.setAvailable(request.isAvailable());
    item.setLocation(request.getLocation());
    item.setUserId(request.getOwnerId());

    item = itemRepository.save(item);
    return mapToResponse(item);
  }

  @Transactional(readOnly = true)
  public ItemResponse getItemById(Long id) {
    Item item = itemRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Предмет не найден"));
    return mapToResponse(item);
  }

  @Transactional
  public ItemResponse updateItem(Long id, ItemRequest request) {
    Item item = itemRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Предмет не найден"));

    item.setTitle(request.getTitle());
    item.setDescription(request.getDescription());
    item.setPricePerDay(request.getPricePerDay());
    item.setDepositAmount(request.getDepositAmount());
    item.setCategory(ItemCategory.valueOf(request.getCategory()));
    item.setImages(request.getImages());
    item.setAvailable(request.isAvailable());
    item.setLocation(request.getLocation());

    Item updatedItem = itemRepository.save(item);
    return mapToResponse(updatedItem);
  }

  @Transactional
  public void deleteItem(Long id) {
    if (!itemRepository.existsById(id)) {
      throw new RuntimeException("Предмет не найден");
    }
    itemRepository.deleteById(id);
  }

  @Transactional(readOnly = true)
  public Page<ItemResponse> getItemsByCategory(ItemCategory category, Pageable pageable) {
    return itemRepository.findByCategory(category, pageable)
        .map(this::mapToResponse);
  }

  @Transactional(readOnly = true)
  public Page<ItemResponse> getItemsByOwner(Long ownerId, Pageable pageable) {
    return itemRepository.findByUserId(ownerId, pageable)
        .map(this::mapToResponse);
  }

  @Transactional(readOnly = true)
  public Page<ItemResponse> getItemsByMaxPrice(BigDecimal maxPrice, Pageable pageable) {
    return itemRepository.findByMaxPrice(maxPrice, pageable)
        .map(this::mapToResponse);
  }

  @Transactional(readOnly = true)
  public List<ItemResponse> getItemsByLocation(String location) {
    return itemRepository.findByLocationContainingIgnoreCase(location)
        .stream()
        .map(this::mapToResponse)
        .toList();
  }

  private ItemResponse mapToResponse(Item item) {
    ItemResponse dto = new ItemResponse();
    dto.setId(item.getId());
    dto.setTitle(item.getTitle());
    dto.setDescription(item.getDescription());
    dto.setPricePerDay(item.getPricePerDay());
    dto.setDepositAmount(item.getDepositAmount());
    dto.setCategory(item.getCategory().name());
    dto.setImages(item.getImages());
    dto.setAvailable(item.isAvailable());
    dto.setLocation(item.getLocation());
    dto.setOwnerId(item.getUserId());
    return dto;
  }
} 
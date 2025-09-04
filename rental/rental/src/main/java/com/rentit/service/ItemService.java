package com.rentit.service;

import com.rentit.dto.ItemDto;
import com.rentit.model.Item;
import com.rentit.model.ItemCategory;
import com.rentit.repository.ItemRepository;
import com.rentit.user.api.UserConnector;
import com.rentit.user.api.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final UserConnector userConnector;

    @Transactional
    public ItemDto createItem(ItemDto itemDto) {
        UserResponse userById = userConnector.getUserById(itemDto.getOwnerId());
        Item item = new Item();
        item.setTitle(itemDto.getTitle());
        item.setDescription(itemDto.getDescription());
        item.setPricePerDay(itemDto.getPricePerDay());
        item.setDepositAmount(itemDto.getDepositAmount());
        item.setCategory(itemDto.getCategory());
        item.setImages(itemDto.getImages());
        item.setAvailable(itemDto.isAvailable());
        item.setLocation(itemDto.getLocation());
        item.setUserId(itemDto.getOwnerId());

        Item savedItem = itemRepository.save(item);
        return convertToDto(savedItem);
    }

    @Transactional(readOnly = true)
    public ItemDto getItemById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Предмет не найден"));
        return convertToDto(item);
    }

    @Transactional
    public ItemDto updateItem(Long id, ItemDto itemDto) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Предмет не найден"));

        item.setTitle(itemDto.getTitle());
        item.setDescription(itemDto.getDescription());
        item.setPricePerDay(itemDto.getPricePerDay());
        item.setDepositAmount(itemDto.getDepositAmount());
        item.setCategory(itemDto.getCategory());
        item.setImages(itemDto.getImages());
        item.setAvailable(itemDto.isAvailable());
        item.setLocation(itemDto.getLocation());

        Item updatedItem = itemRepository.save(item);
        return convertToDto(updatedItem);
    }

    @Transactional
    public void deleteItem(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new RuntimeException("Предмет не найден");
        }
        itemRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<ItemDto> getItemsByCategory(ItemCategory category, Pageable pageable) {
        return itemRepository.findByCategory(category, pageable)
                .map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public Page<ItemDto> getItemsByOwner(Long ownerId, Pageable pageable) {
        return itemRepository.findByUserId(ownerId, pageable)
                .map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public Page<ItemDto> getItemsByMaxPrice(BigDecimal maxPrice, Pageable pageable) {
        return itemRepository.findByMaxPrice(maxPrice, pageable)
                .map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public List<ItemDto> getItemsByLocation(String location) {
        return itemRepository.findByLocationContainingIgnoreCase(location)
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    private ItemDto convertToDto(Item item) {
        ItemDto dto = new ItemDto();
        dto.setId(item.getId());
        dto.setTitle(item.getTitle());
        dto.setDescription(item.getDescription());
        dto.setPricePerDay(item.getPricePerDay());
        dto.setDepositAmount(item.getDepositAmount());
        dto.setCategory(item.getCategory());
        dto.setImages(item.getImages());
        dto.setAvailable(item.isAvailable());
        dto.setLocation(item.getLocation());
        dto.setOwnerId(item.getUserId());
        return dto;
    }
} 
package com.rentit.dto;

import com.rentit.model.ItemCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class ItemDto {
    private Long id;
    
    @NotBlank(message = "Название не может быть пустым")
    private String title;
    
    private String description;
    
    @NotNull(message = "Цена не может быть пустой")
    @Positive(message = "Цена должна быть положительной")
    private BigDecimal pricePerDay;
    
    @Positive(message = "Залог должен быть положительным")
    private BigDecimal depositAmount;
    
    @NotNull(message = "Категория не может быть пустой")
    private ItemCategory category;
    
    private Set<String> images;
    private boolean isAvailable;
    private String location;
    private Long ownerId;
} 
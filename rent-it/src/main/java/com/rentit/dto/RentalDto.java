package com.rentit.dto;

import com.rentit.model.RentalStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RentalDto {
    private Long id;
    
    @NotNull(message = "ID предмета не может быть пустым")
    private Long itemId;
    
    @NotNull(message = "ID арендатора не может быть пустым")
    private Long renterId;
    
    @NotNull(message = "Дата начала не может быть пустой")
    private LocalDateTime startDate;
    
    @NotNull(message = "Дата окончания не может быть пустой")
    private LocalDateTime endDate;
    
    @NotNull(message = "Общая стоимость не может быть пустой")
    @Positive(message = "Общая стоимость должна быть положительной")
    private BigDecimal totalPrice;
    
    @Positive(message = "Залог должен быть положительным")
    private BigDecimal depositAmount;
    
    private RentalStatus status;
} 
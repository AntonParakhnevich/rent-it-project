package com.rentit.model;

public enum RentalStatus {
    PENDING,        // Ожидает подтверждения от владельца
    CONFIRMED,      // Подтверждено владельцем
    IN_PROGRESS,    // Аренда активна
    COMPLETED,      // Аренда завершена
    CANCELLED,      // Аренда отменена
    REJECTED        // Отклонено владельцем
} 
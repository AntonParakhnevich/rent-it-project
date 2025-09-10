package com.rentit.repository;

import com.rentit.model.Item;
import com.rentit.model.ItemCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findByCategory(ItemCategory category, Pageable pageable);
    Page<Item> findByUserId(Long userId, Pageable pageable);
    
    // Поиск доступных товаров
    Page<Item> findByIsAvailable(boolean isAvailable, Pageable pageable);
    
    // Поиск доступных товаров по категории
    @Query("SELECT i FROM Item i WHERE i.isAvailable = true AND i.category = :category")
    Page<Item> findByIsAvailableAndCategory(ItemCategory category, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE i.isAvailable = true AND i.pricePerDay <= :maxPrice")
    Page<Item> findByMaxPrice(BigDecimal maxPrice, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE i.isAvailable = true AND i.category = :category AND i.pricePerDay <= :maxPrice")
    Page<Item> findByCategoryAndMaxPrice(ItemCategory category, BigDecimal maxPrice, Pageable pageable);
    
    // Поиск по местоположению с пагинацией
    @Query("SELECT i FROM Item i WHERE i.isAvailable = true AND LOWER(i.location) LIKE LOWER(CONCAT('%', :location, '%'))")
    Page<Item> findByLocationContainingIgnoreCase(String location, Pageable pageable);
    
    // Оригинальный метод без пагинации для обратной совместимости
    List<Item> findByLocationContainingIgnoreCase(String location);
    
    // Комплексный поиск с несколькими фильтрами
    @Query("SELECT i FROM Item i WHERE i.isAvailable = true " +
           "AND (:category IS NULL OR i.category = :category) " +
           "AND (:maxPrice IS NULL OR i.pricePerDay <= :maxPrice) " +
           "AND (:location IS NULL OR LOWER(i.location) LIKE LOWER(CONCAT('%', :location, '%')))")
    Page<Item> findByFilters(ItemCategory category, BigDecimal maxPrice, String location, Pageable pageable);
} 
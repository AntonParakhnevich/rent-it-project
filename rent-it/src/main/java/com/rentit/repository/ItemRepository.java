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
    
    @Query("SELECT i FROM Item i WHERE i.isAvailable = true AND i.pricePerDay <= :maxPrice")
    Page<Item> findByMaxPrice(BigDecimal maxPrice, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE i.isAvailable = true AND i.category = :category AND i.pricePerDay <= :maxPrice")
    Page<Item> findByCategoryAndMaxPrice(ItemCategory category, BigDecimal maxPrice, Pageable pageable);
    
    List<Item> findByLocationContainingIgnoreCase(String location);
} 
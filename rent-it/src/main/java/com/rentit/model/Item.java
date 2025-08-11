package com.rentit.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private BigDecimal pricePerDay;

    @Column(name = "deposit_amount")
    private BigDecimal depositAmount;

    @JoinColumn(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private ItemCategory category;

    @ElementCollection
    @CollectionTable(name = "item_images", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "image_url")
    private Set<String> images = new HashSet<>();

    @Column(name = "is_available")
    private boolean isAvailable = true;

    @Column(name = "location")
    private String location;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private Set<Rental> rentals = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 
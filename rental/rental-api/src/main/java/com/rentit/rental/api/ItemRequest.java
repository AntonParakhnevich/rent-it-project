package com.rentit.rental.api;

import java.math.BigDecimal;
import java.util.Set;

public class ItemRequest {

  private String title;

  private String description;
  private BigDecimal pricePerDay;

  private BigDecimal depositAmount;

  private String category;

  private Set<String> images;
  private boolean isAvailable;
  private String location;
  private Long ownerId;

  public ItemRequest() {
  }

  public ItemRequest(String title, String description, BigDecimal pricePerDay, BigDecimal depositAmount,
      String category,
      Set<String> images, boolean isAvailable, String location, Long ownerId) {
    this.title = title;
    this.description = description;
    this.pricePerDay = pricePerDay;
    this.depositAmount = depositAmount;
    this.category = category;
    this.images = images;
    this.isAvailable = isAvailable;
    this.location = location;
    this.ownerId = ownerId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BigDecimal getPricePerDay() {
    return pricePerDay;
  }

  public void setPricePerDay(BigDecimal pricePerDay) {
    this.pricePerDay = pricePerDay;
  }

  public BigDecimal getDepositAmount() {
    return depositAmount;
  }

  public void setDepositAmount(BigDecimal depositAmount) {
    this.depositAmount = depositAmount;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public Set<String> getImages() {
    return images;
  }

  public void setImages(Set<String> images) {
    this.images = images;
  }

  public boolean isAvailable() {
    return isAvailable;
  }

  public void setAvailable(boolean available) {
    isAvailable = available;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Long getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(Long ownerId) {
    this.ownerId = ownerId;
  }
}

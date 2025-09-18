package com.rentit.rental.api;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class RentalResponse {

  private Long id;

  private Long itemId;

  private Long renterId;

  private LocalDateTime startDate;

  private LocalDateTime endDate;

  private BigDecimal totalPrice;

  private BigDecimal depositAmount;

  private String status;
  private Long landLordId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getItemId() {
    return itemId;
  }

  public void setItemId(Long itemId) {
    this.itemId = itemId;
  }

  public Long getRenterId() {
    return renterId;
  }

  public void setRenterId(Long renterId) {
    this.renterId = renterId;
  }

  public LocalDateTime getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDateTime startDate) {
    this.startDate = startDate;
  }

  public LocalDateTime getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDateTime endDate) {
    this.endDate = endDate;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(BigDecimal totalPrice) {
    this.totalPrice = totalPrice;
  }

  public BigDecimal getDepositAmount() {
    return depositAmount;
  }

  public void setDepositAmount(BigDecimal depositAmount) {
    this.depositAmount = depositAmount;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Long getLandLordId() {
    return landLordId;
  }

  public void setLandLordId(Long landLordId) {
    this.landLordId = landLordId;
  }
}

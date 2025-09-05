package com.rentit.rental.api;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RentalRequest {

  private Long itemId;

  private Long renterId;

  private LocalDateTime startDate;

  private LocalDateTime endDate;

  private BigDecimal totalPrice;

  private BigDecimal depositAmount;

  private String status;

  public RentalRequest(Long itemId, Long renterId, LocalDateTime startDate, LocalDateTime endDate,
      BigDecimal totalPrice, BigDecimal depositAmount, String status) {
    this.itemId = itemId;
    this.renterId = renterId;
    this.startDate = startDate;
    this.endDate = endDate;
    this.totalPrice = totalPrice;
    this.depositAmount = depositAmount;
    this.status = status;
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
}

package com.rentit.debezium.api;

import java.time.LocalDateTime;

public class RentalCreatedEvent extends Event {

  private Long id;
  private Double totalPrice;
  private Double depositAmount;
  private Long itemId;
  private LocalDateTime dateStart;
  private LocalDateTime dateEnd;

  public RentalCreatedEvent() {
    super();
  }

  public RentalCreatedEvent(String key, Long id, Double totalPrice, Double depositAmount, Long itemId,
      LocalDateTime dateStart, LocalDateTime dateEnd) {
    super(key);
    this.id = id;
    this.totalPrice = totalPrice;
    this.depositAmount = depositAmount;
    this.itemId = itemId;
    this.dateStart = dateStart;
    this.dateEnd = dateEnd;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Double getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(Double totalPrice) {
    this.totalPrice = totalPrice;
  }

  public Double getDepositAmount() {
    return depositAmount;
  }

  public void setDepositAmount(Double depositAmount) {
    this.depositAmount = depositAmount;
  }

  public Long getItemId() {
    return itemId;
  }

  public void setItemId(Long itemId) {
    this.itemId = itemId;
  }

  public LocalDateTime getDateStart() {
    return dateStart;
  }

  public void setDateStart(LocalDateTime dateStart) {
    this.dateStart = dateStart;
  }

  public LocalDateTime getDateEnd() {
    return dateEnd;
  }

  public void setDateEnd(LocalDateTime dateEnd) {
    this.dateEnd = dateEnd;
  }
}

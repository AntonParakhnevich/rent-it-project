package com.rentit.rental.api;

import com.rentit.common.debezium.model.Event;
import java.time.LocalDateTime;

public class RentalCreatedEvent extends Event {

  private Double totalPrice;
  private Double depositAmount;
  private Long itemId;
  private LocalDateTime dateStart;
  private LocalDateTime dateEnd;
  private Long userId;

  public RentalCreatedEvent() {
    super();
  }

  public RentalCreatedEvent(Long id, Double totalPrice, Double depositAmount, Long itemId,
      LocalDateTime dateStart, LocalDateTime dateEnd, Long userId) {
    super(id);
    this.totalPrice = totalPrice;
    this.depositAmount = depositAmount;
    this.itemId = itemId;
    this.dateStart = dateStart;
    this.dateEnd = dateEnd;
    this.userId = userId;
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

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  @Override
  public String toString() {
    return "RentalCreatedEvent{" +
        ", totalPrice=" + totalPrice +
        ", depositAmount=" + depositAmount +
        ", itemId=" + itemId +
        ", dateStart=" + dateStart +
        ", dateEnd=" + dateEnd +
        ", userId=" + userId +
        '}';
  }
}

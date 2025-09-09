package com.rentit.rental.api;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class UnavailableDateItemResponse {

  private Long itemId;
  private Set<LocalDate> unavailableDates;

  public UnavailableDateItemResponse(Long itemId, Set<LocalDate> unavailableDates) {
    this.itemId = itemId;
    this.unavailableDates = unavailableDates;
  }

  public UnavailableDateItemResponse() {
  }

  public Long getItemId() {
    return itemId;
  }

  public void setItemId(Long itemId) {
    this.itemId = itemId;
  }

  public Set<LocalDate> getUnavailableDates() {
    return unavailableDates;
  }

  public void setUnavailableDates(Set<LocalDate> unavailableDates) {
    this.unavailableDates = unavailableDates;
  }
}

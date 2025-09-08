package com.rentit.tax.model;

import java.util.stream.Stream;

public enum TaxStatus {

  ERROR("Ошибка запроса"),
  NOT_FOUND("не найден"),
  ACTIVE("Действующий"),
  PROCESS_LIQUIDATION("В процессе ликвидации"),
  LIQUIDATION("Ликвидирован");

  private String value;

  TaxStatus(String value) {
    this.value = value;
  }

  public String getName() {
    return value;
  }

  public static TaxStatus getStatusByValue(String value) {
    if (value == null || value.isEmpty()) {
      return null;
    }

    return Stream.of(TaxStatus.values())
        .filter(s -> s.value.equals(value))
        .findFirst().orElse(null);
  }
}

package com.rentit.tax.model;

public enum TaxStatus {


  ACTIVE("Действующий"), PROCESS_LIQUIDATION("В процессе ликвидации"), LIQUIDATION("Ликвидирован");

  private String value;


  TaxStatus(String value) {
    this.value = value;
  }

  public String getName() {
    return value;
  }
}

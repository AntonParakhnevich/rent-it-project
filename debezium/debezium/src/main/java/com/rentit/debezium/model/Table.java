package com.rentit.debezium.model;

public enum Table {
  RENTALS("rentals"), ITEMS("items");

  private final String name;

  Table(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}

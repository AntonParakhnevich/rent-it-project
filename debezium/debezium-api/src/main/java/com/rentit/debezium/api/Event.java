package com.rentit.debezium.api;

public abstract class Event {

  private String key;

  public Event() {
  }

  public Event(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }
}

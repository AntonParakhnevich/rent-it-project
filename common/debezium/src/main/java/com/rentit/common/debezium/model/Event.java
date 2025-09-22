package com.rentit.common.debezium.model;

public abstract class Event {

  private Long id;

  public Event(Long id) {
    this.id = id;
  }

  public Event() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "Event{" +
        "id=" + id +
        '}';
  }
}

package com.rentit.debezium.model;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Map;

public class ChangeRecordEvent {

  private String operation;
  private String table;
  private Map<String, Object> after;
  private Map<String, Object> before;

  public ChangeRecordEvent(String operation, String table, Map<String, Object> after, Map<String, Object> before) {
    this.operation = operation;
    this.table = table;
    this.after = after;
    this.before = before;
  }

  public ChangeRecordEvent() {
  }

  public String getOperation() {
    return operation;
  }

  public void setOperation(String operation) {
    this.operation = operation;
  }

  public String getTable() {
    return table;
  }

  public void setTable(String table) {
    this.table = table;
  }

  public Map<String, Object> getAfter() {
    return after;
  }

  public void setAfter(Map<String, Object> after) {
    this.after = after;
  }

  public Map<String, Object> getBefore() {
    return before;
  }

  public void setBefore(Map<String, Object> before) {
    this.before = before;
  }
}

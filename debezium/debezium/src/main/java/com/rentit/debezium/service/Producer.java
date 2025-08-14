package com.rentit.debezium.service;

import com.rentit.debezium.api.Event;
import com.rentit.debezium.model.ChangeRecordEvent;

public interface Producer {
  String tableName();

  public Event produce(ChangeRecordEvent event);

}

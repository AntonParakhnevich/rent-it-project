package com.rentit.common.debezium.service;

import com.rentit.common.debezium.model.ChangeRecordEvent;

public interface Producer {

  String tableName();

  void produce(ChangeRecordEvent event);
}

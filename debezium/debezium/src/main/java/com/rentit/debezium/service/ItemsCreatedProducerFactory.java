package com.rentit.debezium.service;

import com.rentit.debezium.api.Event;
import com.rentit.debezium.model.ChangeRecordEvent;
import com.rentit.debezium.model.Table;
import org.springframework.stereotype.Service;

@Service
public class ItemsCreatedProducerFactory implements Producer{

  @Override
  public String tableName() {
    return Table.ITEMS.getName();
  }

  @Override
  public void produce(ChangeRecordEvent event) {
    return;
  }
}

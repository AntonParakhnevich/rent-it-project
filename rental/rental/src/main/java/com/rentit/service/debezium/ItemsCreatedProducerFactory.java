package com.rentit.service.debezium;

import com.rentit.common.debezium.model.ChangeRecordEvent;
import com.rentit.common.debezium.service.Producer;
import org.springframework.stereotype.Service;

@Service
public class ItemsCreatedProducerFactory implements Producer {

  private final String tableName = "items";

  @Override
  public String tableName() {
    return tableName;
  }

  @Override
  public void produce(ChangeRecordEvent event) {
    return;
  }

}

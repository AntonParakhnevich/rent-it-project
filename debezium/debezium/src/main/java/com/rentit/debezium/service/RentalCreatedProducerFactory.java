package com.rentit.debezium.service;

import com.rentit.debezium.api.Event;
import com.rentit.debezium.api.RentalCreatedEvent;
import com.rentit.debezium.model.ChangeRecordEvent;
import com.rentit.debezium.model.Table;
import java.time.ZonedDateTime;
import org.springframework.stereotype.Service;

@Service
public class RentalCreatedProducerFactory implements Producer {

  @Override
  public String tableName() {
    return Table.RENTALS.getName();
  }

  @Override
  public Event produce(ChangeRecordEvent event) {
    if (!isAvailable(event)) {
      return null;
    }
    RentalCreatedEvent rentalCreatedEvent = new RentalCreatedEvent();
    rentalCreatedEvent.setId(Long.valueOf(event.getAfter().get("id").toString()));

    // Обработка числовых полей с проверкой на null
    if (event.getAfter().get("deposit_amount") != null) {
      rentalCreatedEvent.setDepositAmount(Double.parseDouble(event.getAfter().get("deposit_amount").toString()));
    }
    if (event.getAfter().get("total_price") != null) {
      rentalCreatedEvent.setTotalPrice(Double.parseDouble(event.getAfter().get("total_price").toString()));
    }
    if (event.getAfter().get("item_id") != null) {
      rentalCreatedEvent.setItemId(Long.valueOf(event.getAfter().get("item_id").toString()));
    }

    if (event.getAfter().get("start_date") != null) {
      rentalCreatedEvent.setDateStart(
          ZonedDateTime.parse(event.getAfter().get("start_date").toString()).toLocalDateTime());
    }
    if (event.getAfter().get("end_date") != null) {
      rentalCreatedEvent.setDateEnd(ZonedDateTime.parse(event.getAfter().get("end_date").toString()).toLocalDateTime());
    }
    if (event.getAfter().get("user_id") != null) {
      rentalCreatedEvent.setUserId(Long.valueOf(event.getAfter().get("user_id").toString()));
    }
    rentalCreatedEvent.setKey("RENTAL_CREATED");

    return rentalCreatedEvent;
  }

  private boolean isAvailable(ChangeRecordEvent event) {
    return event.getTable().equals(tableName()) && event.getOperation().equals("c");
  }
}

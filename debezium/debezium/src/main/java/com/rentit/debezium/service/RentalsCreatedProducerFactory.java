package com.rentit.debezium.service;

import com.rentit.debezium.api.Event;
import com.rentit.debezium.api.RentalCreatedEvent;
import com.rentit.debezium.model.ChangeRecordEvent;
import com.rentit.debezium.model.Table;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;

@Service
public class RentalsCreatedProducerFactory implements Producer {

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

    // Обработка временных полей - Debezium может передавать их как timestamp или строки
    if (event.getAfter().get("start_date") != null) {
      rentalCreatedEvent.setDateStart(ZonedDateTime.parse(event.getAfter().get("start_date").toString()).toLocalDateTime());
    }
    if (event.getAfter().get("end_date") != null) {
      rentalCreatedEvent.setDateEnd(ZonedDateTime.parse(event.getAfter().get("end_date").toString()).toLocalDateTime());
    }
    rentalCreatedEvent.setKey("RENTALS_CREATED");

    return rentalCreatedEvent;
  }

  private boolean isAvailable(ChangeRecordEvent event) {
    return event.getTable().equals(tableName()) && event.getOperation().equals("c");
  }
}

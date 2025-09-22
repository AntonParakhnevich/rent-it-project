package com.rentit.service.debezium;

import com.rentit.common.debezium.model.ChangeRecordEvent;
import com.rentit.common.debezium.model.Event;
import com.rentit.common.debezium.service.Producer;
import com.rentit.rental.api.RentalCreatedEvent;
import java.time.ZonedDateTime;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

@Service
public class RentalCreatedProducer implements Producer {

  private final static String TOPIC_NAME = "RENTAL_CREATED";
  private final String tableName = "rentals";

  private final KafkaProducer<String, Event> kafkaProducer;

  public RentalCreatedProducer(KafkaProducer<String, Event> kafkaProducer) {
    this.kafkaProducer = kafkaProducer;
  }

  @Override
  public String tableName() {
    return tableName;
  }

  @Override
  public void produce(ChangeRecordEvent event) {
    if (!isAvailable(event)) {
      return;
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

    kafkaProducer.send(new ProducerRecord<>(TOPIC_NAME, rentalCreatedEvent.getId().toString(), rentalCreatedEvent));
  }

  private boolean isAvailable(ChangeRecordEvent event) {
    return event.getTable().equals(tableName()) && event.getOperation().equals("c");
  }
}

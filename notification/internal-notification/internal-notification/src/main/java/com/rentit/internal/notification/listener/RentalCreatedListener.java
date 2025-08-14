package com.rentit.internal.notification.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class RentalCreatedListener {

  private static final Logger log = LoggerFactory.getLogger(RentalCreatedListener.class);

  @KafkaListener(id = "demoGroup", topics = "RENTALS_UPDATED")
  public void listen(String message) {
    log.info("Received: " + message);
  }

}

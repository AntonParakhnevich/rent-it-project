package com.rentit.internal.notification.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rentit.internal.notification.dto.InternalNotificationModel;
import com.rentit.internal.notification.service.InternalNotificationService;
import com.rentit.internal.notification.service.rentalscreated.RentalsCreatedNotificationService;
import com.rentit.rental.api.RentalCreatedEvent;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class RentalCreatedListener {

  private final ObjectMapper objectMapper;
  private final InternalNotificationService internalNotificationService;
  private final List<RentalsCreatedNotificationService> services;

  private static final Logger log = LoggerFactory.getLogger(RentalCreatedListener.class);

  public RentalCreatedListener(ObjectMapper objectMapper,
      InternalNotificationService internalNotificationService,
      List<RentalsCreatedNotificationService> services) {
    this.objectMapper = objectMapper;
    this.internalNotificationService = internalNotificationService;
    this.services = services;
  }

  @KafkaListener(id = "rentit-debezium-produce", topics = "RENTAL_CREATED")
  public void listen(String message) {
    try {
      RentalCreatedEvent event = objectMapper.readValue(message, RentalCreatedEvent.class);
      log.info("RENTALS_CREATED_EVENT listen: " + event);
      List<InternalNotificationModel> notificationModels = services.stream()
          .map(service -> service.createNotificationModel(event))
          .collect(Collectors.toList());
      internalNotificationService.createList(notificationModels);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}

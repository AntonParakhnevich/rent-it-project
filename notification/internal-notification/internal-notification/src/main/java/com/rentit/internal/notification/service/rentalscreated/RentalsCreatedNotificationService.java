package com.rentit.internal.notification.service.rentalscreated;

import com.rentit.debezium.api.RentalCreatedEvent;
import com.rentit.internal.notification.dto.InternalNotificationModel;

public interface RentalsCreatedNotificationService {

  InternalNotificationModel createNotificationModel(RentalCreatedEvent event);
}

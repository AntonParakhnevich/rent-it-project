package com.rentit.internal.notification.service.rentalscreated;

import com.rentit.internal.notification.dto.InternalNotificationModel;
import com.rentit.rental.api.RentalCreatedEvent;

public interface RentalsCreatedNotificationService {

  InternalNotificationModel createNotificationModel(RentalCreatedEvent event);
}

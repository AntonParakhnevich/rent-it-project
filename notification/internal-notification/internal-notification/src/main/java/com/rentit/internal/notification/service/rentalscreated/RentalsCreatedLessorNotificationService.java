package com.rentit.internal.notification.service.rentalscreated;

import com.rentit.internal.notification.dto.InternalNotificationModel;
import com.rentit.internal.notification.model.NotificationType;
import com.rentit.rental.api.RentalCreatedEvent;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class RentalsCreatedLessorNotificationService implements RentalsCreatedNotificationService {

  @Override
  public InternalNotificationModel createNotificationModel(RentalCreatedEvent event) {
    InternalNotificationModel internalNotificationModel = new InternalNotificationModel();
    internalNotificationModel.setDateCreated(LocalDateTime.now());
    internalNotificationModel.setViewed(false);
    internalNotificationModel.setType(NotificationType.RENTAL_CREATED);
    internalNotificationModel.setUserId(event.getUserId());
    internalNotificationModel.setMessage("Test message lessor");
    return internalNotificationModel;
  }
}

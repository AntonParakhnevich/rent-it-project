package com.rentit.internal.notification.repository;

import com.rentit.internal.notification.model.InternalNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InternalNotificationRepository extends JpaRepository<InternalNotification, Long> {

  Page<InternalNotification> findByUserId(Long userId, Pageable pageable);

}

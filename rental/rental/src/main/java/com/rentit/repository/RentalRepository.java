package com.rentit.repository;

import com.rentit.model.Rental;
import com.rentit.model.RentalStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

  Page<Rental> findByUserId(Long userId, Pageable pageable);

  Page<Rental> findByItemUserId(Long userId, Pageable pageable);

  List<Rental> findByUserId(Long userId);

  @Query("SELECT r FROM Rental r WHERE r.item.id = :itemId AND r.status = :status")
  List<Rental> findByItemIdAndStatus(Long itemId, RentalStatus status);

  @Query("SELECT r FROM Rental r WHERE r.item.id = :itemId AND r.status IN :statuses")
  List<Rental> findByItemIdAndStatusIn(Long itemId, List<RentalStatus> statuses);

  @Query("SELECT r FROM Rental r WHERE r.item.id = :itemId AND " +
      "((r.startDate <= :endDate AND r.endDate >= :startDate) OR " +
      "(r.startDate >= :startDate AND r.startDate <= :endDate))")
  List<Rental> findOverlappingRentals(Long itemId, LocalDateTime startDate, LocalDateTime endDate);

  List<Rental> findByItemIdAndStatusInAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByStartDateAsc(
      Long itemId, Collection<RentalStatus> statuses, LocalDateTime firstDate, LocalDateTime secondDate);
} 
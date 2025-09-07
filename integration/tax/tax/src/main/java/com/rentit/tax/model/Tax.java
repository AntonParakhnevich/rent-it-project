package com.rentit.tax.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.boot.autoconfigure.web.WebProperties.Resources.Chain.Strategy;

@Data
@Entity
@Table(name = "tax")
public class Tax {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "user_id")
  private Long userId;
  @Column(name = "date_requested")
  private LocalDateTime dateRequested;
}

package com.rentit.common.debezium.service;

import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DebeziumEngineRunner {
  private static final Logger log = LoggerFactory.getLogger(DebeziumEngineRunner.class);

  private final DebeziumEngine<ChangeEvent<String, String>> engine;
  private final ExecutorService executorService = Executors.newSingleThreadExecutor(r -> new Thread(r, "debezium-mysql-engine"));

  public DebeziumEngineRunner(DebeziumEngine<ChangeEvent<String, String>> engine) {
    this.engine = engine;
  }

  @PostConstruct
  public void startEngine() {
    executorService.submit(engine);
    log.info("Debezium engine submitted to executor");
  }

  @PreDestroy
  public void stopEngine() {
    try {
      engine.close();
    } catch (IOException e) {
      log.warn("Failed to close Debezium engine", e);
    } finally {
      executorService.shutdownNow();
    }
  }
}



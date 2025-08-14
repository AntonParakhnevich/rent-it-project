package com.rentit.debezium.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rentit.debezium.api.Event;
import com.rentit.debezium.model.ChangeRecordEvent;
import com.rentit.debezium.service.Producer;
import io.debezium.engine.ChangeEvent;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CdcEventPublisher {

  private static final Logger log = LoggerFactory.getLogger(CdcEventPublisher.class);

  private final KafkaProducer<String, Event> kafkaProducer;
  private final ObjectMapper objectMapper;
  private final String rentalsCreatedTopic;
  private final List<Producer> tableNameToProducers;
  private Map<String, List<Producer>> tableNameToProducersMap;

  @PostConstruct
  public void startEngine() {
    tableNameToProducersMap = tableNameToProducers.stream()
        .collect(Collectors.groupingBy(Producer::tableName));
    log.info("Debezium engine submitted to executor");
  }

  public CdcEventPublisher(
      KafkaProducer<String, Event> kafkaProducer,
      ObjectMapper objectMapper,
      @Value("${kafka.topics.rentalsCreated:RENTALS_CREATED}") String rentalsCreatedTopic,
      List<Producer> tableNameToProducers) {
    this.kafkaProducer = kafkaProducer;
    this.objectMapper = objectMapper;
    this.rentalsCreatedTopic = rentalsCreatedTopic;
    this.tableNameToProducers = tableNameToProducers;
  }

  public void handle(ChangeEvent<String, String> event) {
    try {
      JsonNode root = objectMapper.readTree(event.value());

      Map<String, Object> eventMap = objectMapper.convertValue(root, new TypeReference<>() {
      });
      Map<String, Object> payload = objectMapper.convertValue(eventMap.get("payload"), new TypeReference<>() {
      });
      Map<String, Object> after = objectMapper.convertValue(payload.get("after"), new TypeReference<>() {
      });
      Map<String, Object> before = objectMapper.convertValue(payload.get("before"), new TypeReference<>() {
      });
      Map<String, Object> source = objectMapper.convertValue(payload.get("source"), new TypeReference<>() {
      });

      String table = source.get("table").toString();

      ChangeRecordEvent changeRecordEvent = new ChangeRecordEvent();
      changeRecordEvent.setTable(table);
      changeRecordEvent.setAfter(after);
      changeRecordEvent.setBefore(before);
      changeRecordEvent.setOperation(payload.get("op").toString());

      tableNameToProducersMap.get(table)
          .forEach(p -> {
            Optional.ofNullable(p.produce(changeRecordEvent))
                .ifPresent(e -> kafkaProducer.send(new ProducerRecord<>(rentalsCreatedTopic, e.getKey(), e)));
          });
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}



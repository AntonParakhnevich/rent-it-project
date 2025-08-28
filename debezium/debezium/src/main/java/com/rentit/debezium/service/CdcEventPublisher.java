package com.rentit.debezium.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rentit.debezium.model.ChangeRecordEvent;
import io.debezium.engine.ChangeEvent;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class CdcEventPublisher {

  private final ObjectMapper objectMapper;
  private final Map<String, List<Producer>> tableNameToProducersMap;

  public CdcEventPublisher(
      ObjectMapper objectMapper,
      List<Producer> tableNameToProducers
  ) {
    this.objectMapper = objectMapper;
    this.tableNameToProducersMap = tableNameToProducers.stream()
        .collect(Collectors.groupingBy(Producer::tableName));
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
          .forEach(producer -> producer.produce(changeRecordEvent));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}



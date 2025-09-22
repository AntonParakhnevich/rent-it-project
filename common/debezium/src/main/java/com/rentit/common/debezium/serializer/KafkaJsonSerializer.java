package com.rentit.common.debezium.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rentit.common.debezium.model.Event;
import org.apache.kafka.common.serialization.Serializer;

public class KafkaJsonSerializer implements Serializer<Event> {

  private final ObjectMapper objectMapper;

  public KafkaJsonSerializer(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public byte[] serialize(String topic, Event data) {
    try {
      return objectMapper.writeValueAsBytes(data);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}

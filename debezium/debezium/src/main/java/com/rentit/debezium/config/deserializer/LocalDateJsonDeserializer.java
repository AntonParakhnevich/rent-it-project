package com.rentit.debezium.config.deserializer;

import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LocalDateJsonDeserializer extends JsonDeserializer<LocalDate> {

  private static final Logger log = LogManager.getLogger();
  private final List<DateTimeFormatter> formatters;

  public LocalDateJsonDeserializer() {
    this(
        "yyyy-MM-dd'T'HH:mm:ss",
        "yyyy-MM-dd",
        "yyyy.MM.dd",
        "dd.MM.yyyy",
        "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
        "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS",
        "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSSZ"
    );
  }

  public LocalDateJsonDeserializer(String first, String... formatters) {
    this.formatters = Stream
        .concat(Stream.of(first), Stream.of(formatters))
        .map(DateTimeFormatter::ofPattern)
        .collect(toList());
  }

  @Override
  public LocalDate deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
    ObjectCodec oc = jp.getCodec();
    JsonNode node = oc.readTree(jp);
    if (node.isLong()) {
      return Instant.ofEpochMilli(node.longValue()).atZone(ZoneId.systemDefault()).toLocalDate();
    }
    return dateFromString(node.textValue());
  }

  private LocalDate dateFromString(String str) {
    if (str == null) {
      return null;
    }
    for (DateTimeFormatter formatter : formatters) {
      try {
        return LocalDate.parse(str, formatter);
      } catch (DateTimeParseException e) {
        log.debug(e);
      }
    }
    return null;
  }
}
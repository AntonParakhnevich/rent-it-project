package com.rentit.debezium.config.deserializer;

import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LocalDateTimeJsonDeserializer extends JsonDeserializer<LocalDateTime> {

  private static final String[] DEFAULT_FORMATS = new String[]{
      "yyyy-MM-dd'T'HH:mm:ss",
      "yyyy-MM-dd'T'HH:mm",
      "yyyy-MM-dd HH:mm:ss",
      "yyyy-MM-dd'T'HH:mm:ss'Z'",
      "yyyy-MM-dd'T'HH:mm:ss.S",
      "yyyy-MM-dd'T'HH:mm:ss.SS",
      "yyyy-MM-dd'T'HH:mm:ss.SSS",
      "yyyy-MM-dd'T'HH:mm:ss.SSSS",
      "yyyy-MM-dd'T'HH:mm:ss.SSSSS",
      "yyyy-MM-dd'T'HH:mm:ss.SSSSSS",
      "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS",
      "yyyy-MM-dd'T'HH:mm:ss.S'Z'",
      "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
      "yyyy-MM-dd'T'HH:mm:ss.SSSS'Z'",
      "yyyy-MM-dd'T'HH:mm:ss.SSSSS'Z'",
      "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
      "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS'Z'",
      "yyyy-MM-dd'T'HH:mm:ss'Z'",
      "yyyy-MM-dd'T'HH:mm:ss.SSZ",
      "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
      "yyyy-MM-dd'T'HH:mm:ss.SSSSZ",
      "yyyy-MM-dd'T'HH:mm:ss.SSSSSZ",
      "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ",
      "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSSZ"
  };
  private static final Logger log = LogManager.getLogger();
  private final List<DateTimeFormatter> formatters;

  public LocalDateTimeJsonDeserializer() {
    this(DEFAULT_FORMATS);
  }

  public LocalDateTimeJsonDeserializer(String... formatters) {
    this.formatters = Arrays.stream(formatters)
        .map(DateTimeFormatter::ofPattern)
        .collect(toList());
  }

  public LocalDateTimeJsonDeserializer(List<DateTimeFormatter> formatters) {
    this.formatters = formatters;
  }

  public LocalDateTimeJsonDeserializer withDefaultFormatters() {
    List<DateTimeFormatter> dateTimeFormatters = Stream.concat(
            this.formatters.stream(),
            Stream.of(DEFAULT_FORMATS).map(DateTimeFormatter::ofPattern)
        )
        .collect(toList());
    return new LocalDateTimeJsonDeserializer(dateTimeFormatters);
  }

  @Override
  public LocalDateTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
    JsonNode node = jp.readValueAsTree();

    if (node.isLong()) {
      return dateFromLong(node.asLong());
    }

    if (node.isObject()) {
      JsonNode date = node.get("$date");
      if (date.isLong()) {
        return dateFromLong(date.asLong());
      }
      return dateFromString(date.asText());
    }

    return dateFromString(node.textValue());
  }

  private LocalDateTime dateFromLong(Long value) {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(value), TimeZone.getDefault().toZoneId());
  }

  private LocalDateTime dateFromString(String str) {
    if (str == null) {
      return null;
    }
    LocalDateTime result = fastDateFromString(str);
    if (result != null) {
      return result;
    }
    result = parseLocalDateTime(str);
    if (result != null) {
      return result;
    }
    throw new DateTimeParseException("Cannot deserialize date: " + str, str, 0);
  }

  protected LocalDateTime parseLocalDateTime(String str) {
    for (DateTimeFormatter formatter : formatters) {
      try {
        return LocalDateTime.parse(str, formatter);
      } catch (DateTimeParseException e) {
        log.debug(e);
      }
    }
    return null;
  }

  protected LocalDateTime fastDateFromString(String str) {
    // fast yyyy-MM-dd'T'HH:mm:ss'Z'
    if (str.charAt(4) == '-'
        && str.charAt(7) == '-'
        && str.charAt(10) == 'T'
        && str.charAt(13) == ':'
        && str.charAt(16) == ':') {
      int nanos = retrieveNanos(str);
      return LocalDateTime.of(
          Integer.parseInt(str.substring(0, 4)),
          Integer.parseInt(str.substring(5, 7)),
          Integer.parseInt(str.substring(8, 10)),
          Integer.parseInt(str.substring(11, 13)),
          Integer.parseInt(str.substring(14, 16)),
          Integer.parseInt(str.substring(17, 19)),
          nanos
      );
    }
    return null;
  }

  private int retrieveNanos(String str) {
    int nanos = 0;
    if (
        str.length() > 19 &&
            str.length() < 30 &&
            str.charAt(19) == '.'
    ) {
      String numbers = getNumbers(str.substring(20));
      if (numbers.length() == 0) {
        return nanos;
      }
      nanos = Integer.parseInt(numbers);
      nanos *= Math.pow(10, 9.0 - numbers.length());
    }
    return nanos;
  }

  private String getNumbers(String text) {
    StringBuilder sb = new StringBuilder();
    char current;
    for (int i = 0; i <= text.length() - 1; i++) {
      current = text.charAt(i);
      if (!Character.isDigit(current)) {
        return sb.toString();
      }
      sb.append(current);
    }
    return sb.toString();
  }
}
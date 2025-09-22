package com.rentit.common.debezium.config;

import com.rentit.common.debezium.serializer.KafkaJsonSerializer;
import com.rentit.common.debezium.service.CdcEventPublisher;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MySqlDebeziumEngineConfig {

  private final CdcEventPublisher cdcEventPublisher;

  public MySqlDebeziumEngineConfig(CdcEventPublisher cdcEventPublisher) {
    this.cdcEventPublisher = cdcEventPublisher;
  }

  @Value("${debezium.mysql.hostname}")
  private String hostname;

  @Value("${debezium.mysql.port}")
  private int port;

  @Value("${debezium.mysql.user}")
  private String user;

  @Value("${debezium.mysql.password}")
  private String password;

  @Value("${debezium.mysql.serverId}")
  private long serverId;

  @Value("${debezium.mysql.topicPrefix}")
  private String topicPrefix;

  @Value("${debezium.mysql.database}")
  private String database;

  @Value("${debezium.mysql.tableIncludeList}")
  private String tableIncludeList;

  @Value("${debezium.mysql.offsetFile}")
  private String offsetFile;

  @Value("${debezium.mysql.historyFile}")
  private String historyFile;

  @Value("${debezium.mysql.snapshotMode}")
  private String snapshotMode;

  @Value("${debezium.mysql.connectionTimeZone}")
  private String connectionTimeZone;

  @Value("${debezium.mysql.serverTimezone}")
  private String serverTimezone;

  @Bean
  public DebeziumEngine<ChangeEvent<String, String>> mysqlDebeziumEngine() throws IOException {
    ensureParentDirs(offsetFile);
    ensureParentDirs(historyFile);

    Properties props = new Properties();
    // Engine/offset settings
    props.setProperty("name", "rentit-mysql-connector");
    props.setProperty("offset.storage.file.filename", offsetFile);
    props.setProperty("offset.flush.interval.ms", "1000");

    // Connector
    props.setProperty("connector.class", "io.debezium.connector.mysql.MySqlConnector");
    props.setProperty("database.hostname", hostname);
    props.setProperty("database.port", String.valueOf(port));
    props.setProperty("database.user", user);
    props.setProperty("database.password", password);
    props.setProperty("database.serverTimezone", "Europe/Moscow");
    props.setProperty("topic.prefix", topicPrefix);
    props.setProperty("database.include.list", database);
    props.setProperty("table.include.list", tableIncludeList);
    props.setProperty("database.server.id", String.valueOf(serverId));
    props.setProperty("include.schema.changes", "false");
    props.setProperty("snapshot.mode", snapshotMode);
    props.setProperty("connection.time.zone", "UTC");
    // Передаём decimal как строки, чтобы не терять точность и избегать base64 бинарника
    props.setProperty("decimal.handling.mode", "string");

    // Настройки для временных полей
    // Обрабатываем время как строки ISO 8601, а не как timestamp
    props.setProperty("time.precision.mode", "isostring");

    // Internal schema history for Embedded engine
    props.setProperty("schema.history.internal", "io.debezium.storage.file.history.FileSchemaHistory");
    props.setProperty("schema.history.internal.file.filename", historyFile);

    // MySQL recommended
    props.setProperty("database.allowPublicKeyRetrieval", "true");
    // Fix ambiguous server time zone
    props.setProperty("database.connectionTimeZone", connectionTimeZone);
    props.setProperty("database.serverTimezone", serverTimezone);

    props.setProperty("key.converter", StringDeserializer.class.getName());
    props.setProperty("value.converter", KafkaJsonSerializer.class.getName());

    return DebeziumEngine.create(Json.class)
        .using(props)
        .notifying(cdcEventPublisher::handle)
        .build();
  }

  private static void ensureParentDirs(String path) throws IOException {
    File f = new File(path);
    File parent = f.getParentFile();
    if (parent != null && !parent.exists()) {
      if (!parent.mkdirs()) {
        throw new IOException("Failed to create directories for path: " + path);
      }
    }
  }
}



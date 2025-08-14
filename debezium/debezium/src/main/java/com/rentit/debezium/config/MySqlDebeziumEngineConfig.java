package com.rentit.debezium.config;

import com.rentit.debezium.config.serializer.KafkaJsonSerializer;
import io.debezium.engine.ChangeEvent;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.format.Json;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class MySqlDebeziumEngineConfig {

  private final CdcEventPublisher cdcEventPublisher;

  public MySqlDebeziumEngineConfig(CdcEventPublisher cdcEventPublisher) {
    this.cdcEventPublisher = cdcEventPublisher;
  }

  @Value("${debezium.mysql.hostname:localhost}")
  private String hostname;

  @Value("${debezium.mysql.port:3306}")
  private int port;

  @Value("${debezium.mysql.user:root}")
  private String user;

  @Value("${debezium.mysql.password:root}")
  private String password;

  @Value("${debezium.mysql.serverId:184054}")
  private long serverId;

  @Value("${debezium.mysql.topicPrefix:rentit}")
  private String topicPrefix;

  @Value("${debezium.mysql.database:rentit}")
  private String database;

  @Value("${debezium.mysql.tableIncludeList:rentit.items,rentit.rentals}")
  private String tableIncludeList;

  @Value("${debezium.mysql.offsetFile:${user.dir}/debezium-data/offsets.dat}")
  private String offsetFile;

  @Value("${debezium.mysql.historyFile:${user.dir}/debezium-data/schema-history.dat}")
  private String historyFile;

  @Value("${debezium.mysql.snapshotMode:initial}")
  private String snapshotMode;

  @Value("${debezium.mysql.connectionTimeZone:UTC}")
  private String connectionTimeZone;

  @Value("${debezium.mysql.serverTimezone:UTC}")
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



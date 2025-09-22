package com.rentit.common.debezium.service;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

public class KafkaAdminInitializer {

  private static final Logger log = LoggerFactory.getLogger(KafkaAdminInitializer.class);

  private final String bootstrapServers;
  private final List<String> topics;

  public KafkaAdminInitializer(
      @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers,
      @Value("${kafka.topics}") List<String> topics) {
    this.bootstrapServers = bootstrapServers;
    this.topics = topics;
  }

  @PostConstruct
  public void ensureTopics() {
    try (AdminClient admin = AdminClient.create(props())) {
      Set<String> existing = admin.listTopics().names().get();
      List<NewTopic> toCreate = new ArrayList<>();
      topics.stream()
          .filter(topic -> !existing.contains(topic))
          .map(topic -> new NewTopic(topic, 3, (short) 1))
          .forEach(toCreate::add);

      if (!toCreate.isEmpty()) {
        admin.createTopics(toCreate).all().get();
        Set<String> created = new HashSet<>();
        toCreate.forEach(t -> created.add(t.name()));
        log.info("Created Kafka topics: {}", created);
      } else {
        log.info("All Kafka topics already exist");
      }
    } catch (Exception e) {
      log.warn("Failed to verify/create Kafka topics: {}", e.getMessage());
    }
  }

  private Properties props() {
    Properties props = new Properties();
    props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    return props;
  }
}



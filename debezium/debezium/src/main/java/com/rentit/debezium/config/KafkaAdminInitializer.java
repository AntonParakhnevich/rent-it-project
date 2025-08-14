package com.rentit.debezium.config;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

@Component
public class KafkaAdminInitializer {
  private static final Logger log = LoggerFactory.getLogger(KafkaAdminInitializer.class);

  private final String bootstrapServers;
  private final String rentalsCreatedTopic;
  private final String rentalsUpdatedTopic;
  private final String itemCreatedTopic;

  public KafkaAdminInitializer(
      @Value("${spring.kafka.bootstrap-servers:localhost:9092}") String bootstrapServers,
      @Value("${kafka.topics.rentalsCreated:RENTALS_CREATED}") String rentalsCreatedTopic,
      @Value("${kafka.topics.rentalsUpdated:RENTALS_UPDATED}") String rentalsUpdatedTopic,
      @Value("${kafka.topics.itemCreated:ITEM_CREATED}") String itemCreatedTopic) {
    this.bootstrapServers = bootstrapServers;
    this.rentalsCreatedTopic = rentalsCreatedTopic;
    this.rentalsUpdatedTopic = rentalsUpdatedTopic;
    this.itemCreatedTopic = itemCreatedTopic;
  }

  @PostConstruct
  public void ensureTopics() {
    try (AdminClient admin = AdminClient.create(props())) {
      Set<String> existing = admin.listTopics().names().get();
      List<NewTopic> toCreate = new ArrayList<>();
      if (!existing.contains(rentalsCreatedTopic)) {
        toCreate.add(new NewTopic(rentalsCreatedTopic, 3, (short) 1));
      }
      if (!existing.contains(rentalsUpdatedTopic)) {
        toCreate.add(new NewTopic(rentalsUpdatedTopic, 3, (short) 1));
      }
      if (!existing.contains(itemCreatedTopic)) {
        toCreate.add(new NewTopic(itemCreatedTopic, 3, (short) 1));
      }
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



package com.rentit.debezium;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableConfigurationProperties
@ConfigurationPropertiesScan
@EnableAsync
public class DebeziumApp {

  public static void main(String[] args) {
    SpringApplication.run(DebeziumApp.class, args);
  }


}

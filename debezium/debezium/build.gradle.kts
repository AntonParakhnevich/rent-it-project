plugins {
  id("java")
  id("org.springframework.boot") version "3.2.3"
  id("io.spring.dependency-management") version "1.1.4"
}

group = "com.rentit"
version = "0.0.1-SNAPSHOT"

java {
  sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
  compileOnly {
    extendsFrom(configurations.annotationProcessor.get())
  }
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("io.debezium:debezium-api:3.2.0.Final")
  implementation("io.debezium:debezium-embedded:3.2.0.Final")
  implementation("io.debezium:debezium-connector-mysql:3.2.0.Final")

  implementation(project(mapOf("path" to ":debezium:debezium-api")))

  // Kafka client (plain)
  implementation("org.apache.kafka:kafka-clients:3.9.1")
  // Spring Boot Starters
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("org.springframework.boot:spring-boot-starter-mail")
  implementation("org.springframework.boot:spring-boot-starter-websocket")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.cloud:spring-cloud-starter-openfeign:3.1.9")
  implementation("org.springframework.boot:spring-boot-configuration-processor:3.5.3")

  // Database
  runtimeOnly("com.mysql:mysql-connector-j")

}

tasks.withType<Test> {
  useJUnitPlatform()
}

tasks.withType<JavaCompile> {
  options.encoding = "UTF-8"
}
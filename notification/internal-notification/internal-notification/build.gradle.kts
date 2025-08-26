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

  implementation(project(mapOf("path" to ":debezium:debezium-api")))

  // Spring Boot Starters
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")

  implementation("org.springframework.kafka:spring-kafka:3.3.8")

  implementation(project(mapOf("path" to ":notification:internal-notification:internal-notification-api")))


  runtimeOnly("com.mysql:mysql-connector-j")
}
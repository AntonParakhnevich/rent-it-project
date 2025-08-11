plugins {
  id("java")
  id("org.springframework.boot") version "3.2.3"
  id("io.spring.dependency-management") version "1.1.4"
}

group = "com.rentit.user.api"
version = "0.0.1-SNAPSHOT"

java {
  sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
  mavenCentral()
}


dependencies {
  implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.3.0")
  implementation("org.springframework.boot:spring-boot-starter-validation")

}
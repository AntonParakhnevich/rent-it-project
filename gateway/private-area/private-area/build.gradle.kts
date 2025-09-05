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
  implementation(project(mapOf("path" to ":user:user-api")))
  implementation(project(mapOf("path" to ":rental:rental-api")))

  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
  implementation("org.springframework.boot:spring-boot-starter-websocket")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("org.springframework.data:spring-data-commons")


  implementation("io.jsonwebtoken:jjwt-api:0.11.5")
  runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
  runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

  compileOnly("org.projectlombok:lombok")
  annotationProcessor("org.projectlombok:lombok")

}

dependencyManagement {
  imports {
    mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.1")
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
}

tasks.withType<JavaCompile> {
  options.encoding = "UTF-8"
}
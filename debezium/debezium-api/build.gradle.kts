plugins {
  id("java")
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
}

tasks.withType<JavaCompile> {
  options.encoding = "UTF-8"
}
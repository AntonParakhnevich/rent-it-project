# Rent-It

Платформа для аренды вещей: пользователи могут публиковать объявления, арендовать предметы и управлять арендами. Реализованы роли и авторизация по JWT.

## Технологии
- Java 17, Spring Boot 3.2
- Spring Web, Spring Data JPA (Hibernate)
- Spring Security (JWT)
- MySQL (рекомендуется 8.0+)
- Lombok
- Swagger/OpenAPI (springdoc)

## Возможности
- Регистрация и вход по email/паролю
- Роли: `ADMIN`, `LANDLORD` (арендодатель), `RENTER` (арендатор)
- Доступ к эндпоинтам по ролям
- Swagger UI открыт без авторизации

## Быстрый старт
### Требования
- JDK 17+
- MySQL 8.0+ (рекомендуется) или 5.7 (см. примечание по Flyway ниже)
- Git (опционально)

### Подготовка БД
1. Создайте базу данных:
   ```sql
   CREATE DATABASE rentit CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
2. Проверьте настройки в `rent-it/src/main/resources/application.yml`:
   - JDBC URL, логин и пароль к MySQL
   - По умолчанию:
     ```yaml
     spring:
       datasource:
         url: jdbc:mysql://localhost:3306/rentit?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8
         username: root
         password: root
         driver-class-name: com.mysql.cj.jdbc.Driver
       jpa:
         hibernate:
           ddl-auto: update
     ```

### JWT секрет
Рекомендуется задать секрет через переменную окружения `JWT_SECRET`.
- Windows PowerShell:
  ```powershell
  $env:JWT_SECRET = "ваш-длинный-надежный-seed"
  ```
- Linux/macOS:
  ```bash
  export JWT_SECRET="ваш-длинный-надежный-seed"
  ```

### Сборка и запуск
- Запуск из исходников:
  - Windows:
    ```powershell
    cd rent-it
    .\gradlew.bat clean bootRun
    ```
  - Linux/macOS:
    ```bash
    cd rent-it
    ./gradlew clean bootRun
    ```
- Сборка jar и запуск:
  ```bash
  cd rent-it
  ./gradlew clean bootJar
  java -jar build/libs/rent-it-0.0.1-SNAPSHOT.jar
  ```
Приложение по умолчанию стартует на порту `8080`.

## Swagger (API документация)
- UI: `http://localhost:8080/swagger-ui.html` или `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
Доступ открыт без авторизации.

## Аутентификация и роли
### Эндпоинты
- Регистрация: `POST /api/auth/register`
  Пример тела:
  ```json
  {
    "email": "user@example.com",
    "password": "secret123",
    "firstName": "Ivan",
    "lastName": "Ivanov",
    "phoneNumber": "+79990000000",
    "description": "about me",
    "roles": ["RENTER"]
  }
  ```
- Вход: `POST /api/auth/login`
  ```json
  {
    "email": "user@example.com",
    "password": "secret123"
  }
  ```
- Ответ включает JWT:
  ```json
  {
    "accessToken": "<JWT>",
    "userId": 1,
    "email": "user@example.com",
    "firstName": "Ivan",
    "lastName": "Ivanov",
    "roles": ["RENTER"]
  }
  ```
- Для защищённых запросов используйте заголовок:
  ```
  Authorization: Bearer <JWT>
  ```

### Правила доступа (основные)
- `GET /api/items/**` — доступно всем
- `POST/PUT /api/items/**` — роли `ADMIN`, `LANDLORD`
- `DELETE /api/items/**` — только `ADMIN`
- `POST /api/rentals/**` — роли `ADMIN`, `RENTER`
- `PUT /api/rentals/**` — роли `ADMIN`, `LANDLORD`
- `DELETE /api/users/**` — только `ADMIN`
- Остальные — требуют аутентификации (JWT)

## Структура проекта (основное)
- `rent-it/src/main/java/com/rentit/config` — безопасность (SecurityConfig)
- `rent-it/src/main/java/com/rentit/security` — JWT, фильтр, UserDetailsService
- `rent-it/src/main/java/com/rentit/controller` — REST-контроллеры (в т.ч. `AuthController`)
- `rent-it/src/main/java/com/rentit/service` — бизнес-логика
- `rent-it/src/main/java/com/rentit/model` — сущности (`User`, `Role`, и т.д.)
- `rent-it/src/main/resources/application.yml` — конфигурация

## Примечания по БД и миграциям
В сборке присутствует Flyway. На MySQL 5.7 современные версии Flyway могут не поддерживаться.
- Рекомендуется MySQL 8.0+
- Либо отключите Flyway, добавив в `application.yml`:
  ```yaml
  spring:
    flyway:
      enabled: false
  ```
JPA-конфигурация сейчас использует `ddl-auto: update` (генерация схемы).

## Отладка и типичные проблемы
- Порт 8080 занят: измените порт в `application.yml`:
  ```yaml
  server:
    port: 8081
  ```
- 401/403 на защищённых эндпоинтах: убедитесь, что передаёте заголовок `Authorization: Bearer <JWT>`
- MySQL подключение: проверьте хост, порт, логин/пароль и что создана БД `rentit`

## Лицензия
Вставьте информацию о лицензии при необходимости. 
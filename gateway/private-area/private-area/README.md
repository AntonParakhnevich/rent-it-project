# Private Area Gateway

API Gateway для системы rent-it, обеспечивающий маршрутизацию запросов к микросервисам.

## Конфигурация

Gateway настроен для проксирования запросов к следующим эндпоинтам rent-it сервиса:

### Маршруты

1. **Аренда (Rentals)** - `/api/rentals/**`
   - Проксирует все запросы к RentalController
   - Примеры эндпоинтов:
     - `GET /api/rentals/{id}` - получить аренду по ID
     - `POST /api/rentals` - создать новую аренду
     - `PUT /api/rentals/{id}/status` - обновить статус аренды
     - `GET /api/rentals/renter/{renterId}` - получить аренды арендатора

2. **Предметы (Items)** - `/rentits/items/**`
   - Проксирует все запросы к ItemController
   - Примеры эндпоинтов:
     - `GET /rentits/items/{id}` - получить предмет по ID
     - `POST /rentits/items` - создать новый предмет
     - `GET /rentits/items/category/{category}` - получить предметы по категории
     - `GET /rentits/items/owner/{ownerId}` - получить предметы владельца

3. **Общие запросы** - `/rentits/**`
   - Проксирует все остальные запросы к rent-it сервису

## Настройки

- **Порт Gateway**: 8079
- **Целевой сервис**: http://localhost:8080 (rent-it)
- **CORS**: Включен для всех маршрутов
- **Логирование**: Включено для отладки

## Мониторинг

Доступные эндпоинты актуатора:
- `GET /actuator/health` - статус здоровья Gateway
- `GET /actuator/info` - информация о приложении
- `GET /actuator/gateway` - информация о маршрутах Gateway

## Запуск

```bash
./gradlew bootRun
```

Gateway будет доступен по адресу: http://localhost:8079

## Примеры использования

### Создание аренды через Gateway:
```bash
curl -X POST http://localhost:8079/api/rentals \
  -H "Content-Type: application/json" \
  -d '{"itemId": 1, "renterId": 1, "startDate": "2024-01-01", "endDate": "2024-01-10"}'
```

### Получение предметов по категории:
```bash
curl http://localhost:8079/rentits/items/category/ELECTRONICS
```

### Проверка статуса Gateway:
```bash
curl http://localhost:8079/actuator/health
```

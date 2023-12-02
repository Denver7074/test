# Тестовое задание
Стояла следующая задача:

Спроектировать(продумать формат и ограничения входящих/исходящих параметров) и реализовать REST API, вычисляющее частоту встречи символов по заданной строке. Результат должен быть отсортирован по убыванию количества вхождений символа в заданную строку.

Пример входной строки: “aaaaabcccc”

Пример выходного результата: “a”: 5, “c”: 4, “b”: 1

Требования к решению:
- Java 8+;
- Spring boot 2+;
- Решение должно быть покрыто тестами;
- У решения должна быть документация по запуску и формату входящих/исходящих параметров;
- Код решения необходимо разместить в публичном Github репозитории.

## Требования к запуску:
- Java 17+
- Spring Boot 3.2.0
- PostgreSQL 15-alpine
- Docker 24.0.6

## Запуск:
Клонируйте репозиторий из Git
```bash
   git clone https://github.com/ваш_логин/ваш_репозиторий.git
```
Использую сборщик проектов `Gradle` соберите проект
## Методы:
#### Метод `POST`
- Путь: ``

`@RequestParam`: 

- `text` - строка, для которой нужно рассчитать частоту символов.
- `number` - используемый метод расчета (1 - обычный перебор символов, 2 - с использованием многопоточки)

`@ResponseStatus`:
- `200` - запрос выполнен успешно
- `400` - возникла непредвиденная ошибка

`Exception`:
- `E001` - Параметр 'text' не может быть пустым

Пример cURL запроса:
```bash
curl --location 'http://localhost:8080?text=dsfsffdf&number=2' \
--header 'Content-Type: application/json' \
--data '{
    "test":""
}'
```

Пример ответа:
```json
{
    "isFinish": true,
    "data": {
        "text": "апраррпар",
        "results": [
            {
                "key": "р",
                "value": 4
            },
            {
                "key": "а",
                "value": 3
            },
            {
                "key": "п",
                "value": 2
            }
        ]
    }
}
```
#### Метод `GET`
- Путь: ``
  

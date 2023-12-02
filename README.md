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
## Развертывание PostgreSQL с помощью Docker Compose

Это приложение использует Docker Compose для удобного развертывания и работы с базой данных. Вот пример файла `docker-compose.yml`:
```yaml
version: "3.9"

services:
  postgres:
    container_name: postgres
    image: postgres:15-alpine
    environment:
      POSTGRES_DB: "test"
      POSTGRES_USER: "test"
      POSTGRES_PASSWORD: "test"
    ports:
      - "5432:5432"
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U habrpguser -d habrdb"]
      interval: 10s
      timeout: 5s
      retries: 5
      start_period: 10s
    restart: unless-stopped
    networks:
      - postgres

networks:
  postgres:
    driver: bridge
```

## PostgreSQL

Данное приложение использует СУБД PostgreSQL. Вот параметры конфигурации базы данных:
```bash
spring.datasource.url=jdbc:postgresql://localhost:5432/test
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.username=test
spring.datasource.password=test
spring.datasource.hikari.schema=test
```


## Запуск:
Клонируйте репозиторий из Git
```bash
git clone https://github.com/Denver7074/test.git
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

`@ResponseStatus`:
- `200` - запрос выполнен успешно
- `400` - возникла непредвиденная ошибка

`Exception`:
- `E002` - В базе данных пока ничего нет

Пример cURL запроса:
```bash
curl --location 'http://localhost:8080'
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
## Swagger
```bash
http://localhost:8080/swagger-ui/index.html#/
```
## Тестирование
#### Метод `POST`
1. Проверка на ввод основного параметра метода - строка символов. Строка не должна быть `@NotBlank`
```java
	public void testCountFrequencyConcurrentWithEmptyText() throws Exception {
		mockMvc.perform(post("")
						.param("text", " ")
						.param("number", "2"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.details").value("Параметр 'text' не может быть пустым"));
	}
```
2. Проверка на ввод случайных символов. Для этого создали генератор строки с набором случайных символов. Условие: Каждое последующий символ должен повторяться столькоже или меньше чем предыдущий.
```java
	public void testCountFrequencyConcurrentWithText() throws Exception {
		ArrayNode results = getResult(randomText());

		for (int i = 0; i < results.size() - 1; i++) {
			assertThat(results.get(i).get("value").intValue() >= results.get(i + 1).get("value").intValue());
		}
	}

	private String randomText() {
		String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+-=[]{}|;':,.<>?/";
		Random random = new Random();
		StringBuilder text = new StringBuilder();
		for (int i = 0; i < 10000; i++) {
			char c = characters.charAt(random.nextInt(characters.length()));
			text.append(c);
		}
		return text.toString();
	}

	private ArrayNode getResult(String text) throws Exception {
		MvcResult result = mockMvc.perform(post("")
						.param("text", text)
						.param("number", "2"))
				.andExpect(status().isOk())
				.andReturn();
		String responseBody = result.getResponse().getContentAsString();
		JsonNode jsonNode = new ObjectMapper().readTree(responseBody);
		return  (ArrayNode) jsonNode.get("data").get("results");
	}
```
3. Проверка на ввод определенного набора символов. В данном тесте проверяется входной размер массива после всех действий над символами, а также проверяется конкретный порядок символов.
```java
	public void testCountFrequencyWithText() throws Exception {
		ArrayNode results = getResult("aaaabbbccd");
		assertThat(results).hasSize(4);

		assertThat(results.get(0).get("key").textValue()).isEqualTo("a");
		assertThat(results.get(0).get("value").intValue()).isEqualTo(4);

		assertThat(results.get(1).get("key").textValue()).isEqualTo("b");
		assertThat(results.get(1).get("value").intValue()).isEqualTo(3);

		assertThat(results.get(2).get("key").textValue()).isEqualTo("c");
		assertThat(results.get(2).get("value").intValue()).isEqualTo(2);

		assertThat(results.get(3).get("key").textValue()).isEqualTo("d");
		assertThat(results.get(3).get("value").intValue()).isEqualTo(1);
	}

	private ArrayNode getResult(String text) throws Exception {
		MvcResult result = mockMvc.perform(post("")
						.param("text", text)
						.param("number", "2"))
				.andExpect(status().isOk())
				.andReturn();
		String responseBody = result.getResponse().getContentAsString();
		JsonNode jsonNode = new ObjectMapper().readTree(responseBody);
		return  (ArrayNode) jsonNode.get("data").get("results");
	}
```
#### Метод `GET`
1. Проверка что при взятии всех отработанных строк и их значений из бд также распределяется в порядке убывания.
```java
	public void testGetAll() throws Exception {
		MvcResult result = mockMvc.perform(get(""))
				.andExpect(status().isOk())
				.andReturn();
		String responseBody = result.getResponse().getContentAsString();
		JsonNode jsonNode = new ObjectMapper().readTree(responseBody);
		for (JsonNode j : jsonNode) {
			ArrayNode results = (ArrayNode) j.get("results");
			for (int i = 0; i < results.size() - 1; i++) {
				assertThat(results.get(i).get("value").intValue() >= results.get(i + 1).get("value").intValue());
			}
		}
	}
```

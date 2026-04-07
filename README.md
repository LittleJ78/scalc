# SCalc

`SCalc` — это старый Java/Spring-проект, который сейчас рефакторится из серверного MVC-приложения в API-first сервис. Его основная задача — хранить математические выражения по проектам, вычислять их через собственный движок и отдавать данные через REST API.

Сейчас репозиторий уже не использует Thymeleaf и HTML-представления: входной HTTP-слой вынесен в `presentation`, а доступ к базе данных переведен в `persistent` на `JdbcTemplate`.

## Что умеет приложение

- регистрировать пользователя через REST API;
- выполнять REST-логин;
- возвращать JWT-заглушку как контракт под будущую токен-авторизацию;
- работать с проектами пользователя;
- хранить выражения внутри проектов;
- вычислять выражения без сохранения;
- сохранять вычисленные выражения;
- ссылаться в одних выражениях на другие выражения проекта;
- возвращать плоские API-модели вместо старых JPA-сущностей.

## Технологический стек

- Java 17
- Spring Boot 3.5.13
- Spring Web
- Spring JDBC
- Spring Security
- PostgreSQL
- Maven
- Lombok

## Архитектура

### `local.myproject.scalc.presentation`

HTTP-слой приложения. Он разделен на три подпакета:

- `presentation.controller` — REST-контроллеры;
- `presentation.dto` — DTO внешнего API;
- `presentation.mapper` — преобразование между доменными моделями и DTO.

В `presentation.controller` находятся:

- `AuthController` — регистрация и логин;
- `ProjectApiController` — CRUD проектов;
- `ExpressionApiController` — получение, вычисление, сохранение и обновление выражений.

Все внешние маршруты идут через префикс `/api/v1`.

Также к API подключен Swagger / OpenAPI с русскими описаниями операций и схем.

### `local.myproject.scalc.domain`

Здесь теперь находятся доменные модели, которые отделяют REST-слой от объектов хранения данных:

- `UserModel`
- `ProjectModel`
- `ExpressionUnitModel`

Это переходный доменный слой, куда постепенно переносится предметная логика и внутренние модели приложения.

### `local.myproject.scalc.persistent`

Слой хранения данных. Он разделен на:

- `persistent.dao` — DAO на `JdbcTemplate`;
- `persistent.dto` — плоские DTO для хранения;
- `persistent.mapper` — преобразование между persistence DTO и объектами приложения.

Именно этот слой теперь отвечает за работу с PostgreSQL.

### `local.myproject.scalc.domain`

Здесь живут основные объекты предметной области проекта:

- `User`
- `Project`
- `ExpressionUnit`
- `ParametrisedExpressions`
- `ParametersForExpressions`

Они используются как обычные доменные объекты приложения.

### `local.myproject.scalc.services`

Бизнес-логика приложения:

- работа с пользователями;
- работа с проектами;
- работа с выражениями;
- подготовка параметров выражений;
- интеграция со Spring Security через `UserDetailsServiceImpl`.

### `local.myproject.calculate`

Отдельный вычислительный движок, который разбирает и вычисляет математические выражения. Это наиболее самостоятельная и интересная часть проекта.

Внутри него есть:

- представление выражения и его частей: `Expression`, `Unit`, `Operand`, `Operator`;
- enum-описания операторов и типов;
- валидация, конвертация и математические утилиты;
- консольная точка входа `App`, оставшаяся от раннего этапа проекта.

## Основные сущности

### `User`

Поля:

- `userId`
- `userName`
- `password`
- `confirmPassword`
- `email`
- `roles`
- `projects`

Особенности:

- пароль при сохранении хешируется;
- новому пользователю назначается роль `USER`;
- `confirmPassword` используется как вспомогательное поле при регистрации.

### `Project`

Поля:

- `projectId`
- `name`
- `description`
- `expressionUnits`
- `user`

Один пользователь владеет несколькими проектами.

### `ExpressionUnit`

Главная предметная сущность проекта.

Поля:

- `expressionUnitId`
- `typeOfOperand`
- `expressionUnitName`
- `defaultExpression`
- `expressionResult`
- `project`
- `watchList`

Дополнительно сущность умеет:

- нормализовать выражение;
- вычислять выражение;
- получать атомарные операции;
- выводить результат в нужном формате;
- работать с параметрами, то есть зависимостями от других выражений проекта.

## Как работает вычисление

Класс `Expression` выполняет такой pipeline:

1. Принимает строковое выражение.
2. Нормализует запись.
3. Разбивает строку на отдельные `Unit`-элементы.
4. Уточняет типы операторов, например различает унарный и бинарный `-`.
5. Строит польскую запись.
6. Вычисляет результат через стек.
7. При необходимости формирует список атомарных операций.

Поддерживаются:

- `+`, `-`, `*`, `/`, `^`
- `sin`, `cos`, `tan`, `ctg`
- `ln`, `lg`
- `!`
- `%`
- скобки

В коде также видна поддержка разных типов операндов, включая римские числа.

## REST API

### Аутентификация

- `POST /api/v1/auth/register`
- `POST /api/v1/auth/login`

Логин сейчас возвращает JSON с JWT-заглушкой:

- `token`
- `tokenType`
- `expiresIn`
- `user`

Токен пока не является настоящим подписанным JWT. Это временный контракт под будущую полноценную JWT-аутентификацию.

### Проекты

- `GET /api/v1/projects`
- `POST /api/v1/projects`
- `GET /api/v1/projects/{projectId}`
- `PUT /api/v1/projects/{projectId}`
- `DELETE /api/v1/projects/{projectId}`

### Выражения

- `GET /api/v1/projects/{projectId}/expressions`
- `POST /api/v1/projects/{projectId}/expressions/calculate`
- `POST /api/v1/projects/{projectId}/expressions`
- `GET /api/v1/expressions/{expressionUnitId}`
- `PUT /api/v1/expressions/{expressionUnitId}`
- `DELETE /api/v1/expressions/{expressionUnitId}`

## Безопасность

Текущая конфигурация `SecurityConfig` переведена в REST-режим:

- `csrf` отключен;
- приложение работает как `stateless`;
- `/api/v1/auth/**` доступны без авторизации;
- остальные `/api/v1/**` требуют аутентификацию;
- временно используется `httpBasic()` как мост до полноценного JWT;
- `POST /api/v1/auth/login` возвращает JWT-заглушку, но проверка Bearer-токена еще не реализована.

Иными словами, API-контракт под JWT уже есть, но безопасность пока переходная.

## Конфигурация и запуск

### Требования

- Java 17
- PostgreSQL
- Maven или Maven Wrapper

### Конфигурация БД

Настройки лежат в [application.yml](/Users/littlej/Projects/learning/scalc/src/main/resources/application.yml):

```yaml
spring:
  datasource:
    url: jdbc:postgresql://postgres.local:5432/scalc
    username: postgres
    password: postgres
```

Дополнительно:

- `spring.sql.init.mode=always`

Структура таблиц инициализируется через [schema.sql](/Users/littlej/Projects/learning/scalc/src/main/resources/schema.sql).

### Локальный запуск

1. Поднять PostgreSQL.
2. Убедиться, что доступен хост `postgres.local`.
3. Создать базу `scalc`.
4. При необходимости скорректировать `application.yml`.
5. Запустить приложение:

```bash
./mvnw spring-boot:run
```

Или собрать jar:

```bash
./mvnw clean package
java -jar target/scalc-0.0.1-SNAPSHOT.jar
```

## Структура репозитория

```text
scalc/
├── pom.xml
├── mvnw
├── mvnw.cmd
├── HELP.md
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── local/myproject/
│   │   │       ├── calculate/
│   │   │       └── scalc/
│   │   │           ├── configs/
│   │   │           ├── domain/
│   │   │           ├── persistent/
│   │   │           ├── presentation/
│   │   │           └── services/
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── schema.sql
│   │       └── schema_notused.sql
│   └── test/
│       └── java/
│           └── local/myproject/scalc/
└── target/
```

## Что изменилось в последнем рефакторинге

- добавлен слой `presentation`;
- удалены MVC-контроллеры;
- удалены Thymeleaf-зависимости;
- удалены шаблоны и статический фронтенд;
- логин и регистрация перенесены в REST API;
- добавлены API-модели;
- добавлен `ExpressionUnitModel`;
- безопасность переведена в stateless REST-режим с JWT-заглушкой.
- Spring Data JPA удален, хранение переведено на `JdbcTemplate` через пакет `persistent`.

## Технический долг

Сейчас в проекте еще есть заметные ограничения:

- пароль БД хранится в репозитории;
- JWT пока только заглушка без полноценного фильтра и валидации Bearer-токена;
- `csrf` отключен;
- тестов очень мало;
- в коде по-прежнему много `Optional.get()` без безопасной обработки;
- есть старые артефакты вроде `schema_notused.sql`;
- часть логики все еще живет близко к сущностям, а не в отдельных доменных объектах.

## Что логично делать дальше

1. Реализовать настоящий JWT:
   - генерацию;
   - валидацию;
   - security filter для Bearer-токена.
2. Перевести protected API с `httpBasic` на Bearer-only схему.
3. Добавить DTO-валидацию для auth/project/expression запросов.
4. Начать вынос предметной логики в `domain`.
5. Покрыть вычислительный движок и presentation-слой тестами.
6. Убрать секреты из `application.yml` в переменные окружения.

## Краткий вывод

`SCalc` сейчас уже не MVC-приложение с HTML-формами, а переходный API-first сервис с собственным математическим движком, PostgreSQL и REST-контрактом под будущий JWT. Основная ценность проекта — в вычислительной логике и в постепенной архитектурной миграции от legacy-структуры к более чистому разделению слоев.

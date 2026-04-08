# SCalc

`SCalc` — это учебный Java/Spring-проект, который постепенно был переработан из старого MVC-приложения в API-first сервис. Основная задача приложения — хранить математические выражения по пользовательским проектам, вычислять их через собственный движок и отдавать данные через REST API.

Сейчас проект уже не использует JPA, MVC и Thymeleaf. Вместо этого в репозитории выстроена слоистая архитектура с отдельными пакетами `presentation`, `application`, `domain` и `infrastructure`, а доступ к PostgreSQL реализован через `JdbcTemplate`.

## Что умеет приложение

- регистрировать пользователей через REST API;
- выполнять REST-логин;
- возвращать JWT-заглушку как контракт под будущую токен-авторизацию;
- работать с проектами пользователя;
- хранить выражения внутри проектов;
- вычислять выражения без сохранения;
- сохранять и изменять выражения проекта;
- ссылаться в выражениях на другие выражения проекта;
- возвращать результат расчета вместе с атомарными действиями;
- публиковать OpenAPI / Swagger-документацию на русском языке.

## Технологический стек

- Java 17
- Spring Boot 3.5.13
- Spring Web
- Spring JDBC
- Spring Security
- PostgreSQL
- springdoc-openapi
- Maven
- Lombok

## Архитектура

### `local.myproject.scalc.presentation`

HTTP-слой приложения.

Структура слоя:

- `presentation.config` — конфигурация публичного API и Swagger;
- `presentation.controller` — REST-контроллеры;
- `presentation.dto` — DTO внешнего API;
- `presentation.mapper` — преобразование между DTO и объектами application/domain.

Внутри `controller`, `dto` и `mapper` пакеты дополнительно разрезаны горизонтально по сущностям:

- `user`
- `project`
- `expression`

Основные контроллеры:

- `AuthController` — регистрация и логин;
- `ProjectApiController` — CRUD проектов;
- `ExpressionApiController` — CRUD выражений проекта;
- `ExpressionCalculationController` — разовый расчет выражения без сохранения.

Все внешние маршруты идут через префикс `/api/v1`.

### `local.myproject.scalc.application`

Прикладной слой, в котором сосредоточена orchestration-логика use case-ов.

Он разрезан на три вертикальных слайса:

- `application.user`
- `application.project`
- `application.expression`

Внутри каждого слайса используются типовые подпакеты:

- `command` — команды на изменение состояния;
- `query` — запросы на чтение;
- `mapper` — преобразование данных между слоями;
- `port.in` — входные порты use case-ов;
- `port.out` — исходящие порты к инфраструктуре;
- `usecase` — классы прикладных сценариев.

Примеры use case-ов:

- `AuthUseCase`
- `ProjectUseCase`
- `ExpressionUseCase`
- `ExpressionCalculationUseCase`
- `UserLifecycleUseCase`
- `ProjectLifecycleUseCase`
- `ExpressionLifecycleUseCase`

### `local.myproject.scalc.domain`

Доменный слой больше не является плоским пакетом и разложен по агрегатам:

- `domain.aggregate.user`
- `domain.aggregate.project`
- `domain.aggregate.expression`

Основные доменные модели:

- `User`
- `Role`
- `Project`
- `ExpressionUnit`
- `ParametrisedExpressions`
- `ParametersForExpressions`
- `TreeOfParameter`

Именно эти классы теперь заменяют старые сущности из бывшего пакета `entitys`.

### `local.myproject.scalc.infrastructure`

Технический слой интеграций.

Сейчас в нем два основных направления:

- `infrastructure.persistent` — доступ к данным;
- `infrastructure.security` — интеграция со Spring Security.

#### `infrastructure.persistent`

Persistence-слой разрезан горизонтально:

- `dao`
- `dto`
- `mapper`

И внутри каждого из них классы также сгруппированы по сущностям:

- `user`
- `project`
- `expression`

Работа с БД выполнена через `JdbcTemplate`, без Spring Data JPA.

#### `infrastructure.security`

Здесь сосредоточены технические адаптеры безопасности:

- `SpringSecurityAuthenticationAdapter`
- `UserDetailsServiceImpl`
- `config/SecurityConfig`

### `local.myproject.calculate`

Отдельный вычислительный движок, который разбирает и вычисляет математические выражения. Это самая самостоятельная часть проекта, сохранившаяся еще с раннего этапа разработки.

Внутри находятся:

- представление выражения и его частей: `Expression`, `Unit`, `Operand`, `Operator`;
- enum-описания операторов и типов;
- валидация, конвертация и математические утилиты;
- консольная точка входа `App`.

## Основные доменные модели

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
- `confirmPassword` используется как вспомогательное поле регистрации.

### `Project`

Поля:

- `projectId`
- `name`
- `description`
- `expressionUnits`
- `user`

Один пользователь владеет несколькими проектами.

### `ExpressionUnit`

Главная предметная модель проекта.

Поля:

- `expressionUnitId`
- `typeOfOperand`
- `expressionUnitName`
- `defaultExpression`
- `expressionResult`
- `project`
- `watchList`

Модель участвует в:

- вычислении выражения;
- нормализации записи;
- сохранении результата;
- работе с параметризованными выражениями;
- формировании атомарных действий.

## Как работает вычисление

Класс `Expression` внутри вычислительного движка выполняет такой pipeline:

1. Принимает строковое выражение.
2. Нормализует запись.
3. Разбивает строку на отдельные `Unit`-элементы.
4. Уточняет типы операторов, например различает унарный и бинарный `-`.
5. Строит польскую запись.
6. Вычисляет результат через стек.
7. Формирует список атомарных операций, если это требуется сценарием.

Поддерживаются:

- `+`, `-`, `*`, `/`, `^`
- `sin`, `cos`, `tan`, `ctg`
- `ln`, `lg`
- `!`
- `%`
- скобки

В проекте также присутствует поддержка разных типов операндов, включая римские числа.

## REST API

### Аутентификация

- `POST /api/v1/auth/register`
- `POST /api/v1/auth/login`

Логин возвращает JSON с временной JWT-заглушкой:

- `token`
- `tokenType`
- `expiresIn`
- `user`

Это контракт под будущий JWT, а не полноценный подписанный токен.

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

### Калькулятор

- `POST /api/v1/calculator/evaluate`

Этот endpoint выполняет разовый расчет выражения и возвращает:

- исходное выражение;
- итоговый результат;
- нормализованное представление;
- атомарные действия вычисления.

## Swagger / OpenAPI

Swagger подключен через `springdoc-openapi`.

Полезные адреса:

- `http://localhost:8080/swagger-ui/index.html`
- `http://localhost:8080/v3/api-docs`

Конфигурация OpenAPI находится в `presentation.config.OpenApiConfig`, а описания операций и схем написаны на русском языке.

## Безопасность

Текущая конфигурация безопасности находится в `infrastructure.security.config.SecurityConfig`.

Текущее состояние:

- `csrf` отключен;
- приложение работает в `stateless`-режиме;
- включен `httpBasic()` как временный мост до полноценного JWT;
- login endpoint возвращает JWT-заглушку;
- Bearer-авторизация пока не реализована;
- в текущей сборке правило `.requestMatchers("/**").permitAll()` фактически открывает весь HTTP-входной слой.

То есть security-архитектура уже подготовлена к дальнейшему развитию, но защита API пока остается переходной.

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

Дополнительно используется SQL-инициализация схемы при старте приложения.

Структура таблиц задается в [schema.sql](/Users/littlej/Projects/learning/scalc/src/main/resources/schema.sql).

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
├── README.md
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── local/myproject/
│   │   │       ├── calculate/
│   │   │       └── scalc/
│   │   │           ├── application/
│   │   │           ├── domain/
│   │   │           ├── infrastructure/
│   │   │           └── presentation/
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── schema.sql
│   │       └── schema_notused.sql
│   └── test/
│       └── java/
│           └── local/myproject/scalc/
```

## Что изменилось в ходе рефакторинга

- удалены MVC-контроллеры и Thymeleaf;
- удалены JPA и Spring Data JPA;
- удалены старые пакеты `entitys`, `repositories`, `services`, `configs`;
- введена слоистая архитектура `presentation / application / domain / infrastructure`;
- `application` разрезан по слайсам `user / project / expression`;
- `presentation` и `infrastructure.persistent` разрезаны горизонтально и сгруппированы по сущностям;
- security-интеграции вынесены в `infrastructure.security`;
- конфигурации разнесены по `presentation.config` и `infrastructure.security.config`;
- PostgreSQL подключен через `application.yml`;
- Swagger приведен к REST API и описан на русском языке.

## Технический долг

Сейчас в проекте еще есть заметные ограничения:

- пароль БД хранится в репозитории;
- JWT пока остается заглушкой без полноценного фильтра и валидации Bearer-токена;
- `SecurityConfig` пока фактически открывает все маршруты;
- `csrf` отключен;
- тестов мало;
- часть сообщений об ошибках и legacy-названий еще не вычищена;
- в проекте остался старый артефакт `schema_notused.sql`;
- в доменной модели еще есть места, которые стоит сильнее развязать от прикладного слоя.

## Что логично делать дальше

1. Реализовать настоящий JWT:
   генерацию, валидацию и Bearer-фильтр.
2. Привести `SecurityConfig` к реальному ограничению доступа к защищенным endpoint'ам.
3. Добавить bean validation для входных DTO.
4. Расширить покрытие тестами application-, presentation- и infrastructure-слоев.
5. Убрать секреты из `application.yml` в переменные окружения.
6. Дочистить legacy-артефакты и сообщения об ошибках.

## Краткий вывод

`SCalc` сейчас представляет собой API-first сервис с собственным математическим движком, PostgreSQL и уже довольно глубоко переработанной архитектурой. Главная ценность проекта — вычислительная логика и сам путь миграции от старого монолитного MVC/JPA-приложения к более чистому разделению на presentation, application, domain и infrastructure.

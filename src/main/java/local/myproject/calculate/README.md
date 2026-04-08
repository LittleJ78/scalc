# Calculate Engine

`local.myproject.calculate` — это внутренний математический движок проекта `SCalc`. Он отвечает за разбор строкового алгебраического выражения, нормализацию записи, построение польской записи, вычисление результата и формирование атомарных действий.

Сейчас пакет уже встроен в основную архитектуру приложения через адаптер [LegacyCalculationEngineAdapter.java](/Users/littlej/Projects/learning/scalc/src/main/java/local/myproject/scalc/infrastructure/calculation/LegacyCalculationEngineAdapter.java). Это значит, что остальная часть `scalc` не должна обращаться к внутренним классам движка напрямую, кроме фасада [Expression.java](/Users/littlej/Projects/learning/scalc/src/main/java/local/myproject/calculate/Expression.java) и технической инфраструктурной интеграции.

## Назначение

Движок решает такие задачи:

- принимает выражение в строковом виде;
- нормализует запись и добавляет пробелы вокруг операторов;
- подставляет параметры вида `A`, `B`, `Total_Value`;
- различает бинарные и унарные операторы;
- строит польскую запись;
- вычисляет итоговый результат;
- сохраняет список атомарных операций вычисления;
- умеет представлять результат в разных типах операндов.

## Публичная точка входа

Главный публичный фасад движка — [Expression.java](/Users/littlej/Projects/learning/scalc/src/main/java/local/myproject/calculate/Expression.java).

Именно через него нужно работать с движком:

```java
import local.myproject.calculate.Expression;
import local.myproject.calculate.converter.Converter;

Expression expression = new Expression("2 + 2 * 2");
String normalized = expression.getUnitExpression();
String result = Converter.operandToString(expression.calculate());
```

Если выражение использует параметры:

```java
Expression expression = new Expression(
        "A + 1",
        new String[]{"A", "2"}
);
String result = Converter.operandToString(expression.calculate());
```

## Структура пакета

После рефакторинга пакет разрезан по техническим зонам ответственности.

### Корень `calculate`

- [Expression.java](/Users/littlej/Projects/learning/scalc/src/main/java/local/myproject/calculate/Expression.java) — фасад движка. Координирует внутренние компоненты и предоставляет внешний API.

### `calculate.model`

Модели и базовые элементы выражения.

- [Unit.java](/Users/littlej/Projects/learning/scalc/src/main/java/local/myproject/calculate/model/Unit.java) — базовый абстрактный элемент выражения.
- [Operand.java](/Users/littlej/Projects/learning/scalc/src/main/java/local/myproject/calculate/model/Operand.java) — числовой операнд или параметризованный операнд.
- [Operator.java](/Users/littlej/Projects/learning/scalc/src/main/java/local/myproject/calculate/model/Operator.java) — оператор выражения с типом и приоритетом.

### `calculate.parser`

Компоненты подготовки выражения к вычислению.

- [ExpressionNormalizer.java](/Users/littlej/Projects/learning/scalc/src/main/java/local/myproject/calculate/parser/ExpressionNormalizer.java) — нормализует строку выражения.
- [UnitExpressionParser.java](/Users/littlej/Projects/learning/scalc/src/main/java/local/myproject/calculate/parser/UnitExpressionParser.java) — превращает строку в список `Unit`.
- [OperatorTypeResolver.java](/Users/littlej/Projects/learning/scalc/src/main/java/local/myproject/calculate/parser/OperatorTypeResolver.java) — уточняет тип операторов, например различает унарный и бинарный `-`.
- [PolishRecordBuilder.java](/Users/littlej/Projects/learning/scalc/src/main/java/local/myproject/calculate/parser/PolishRecordBuilder.java) — строит польскую запись.

### `calculate.evaluator`

Вычисление выражения.

- [ExpressionEvaluator.java](/Users/littlej/Projects/learning/scalc/src/main/java/local/myproject/calculate/evaluator/ExpressionEvaluator.java) — вычисляет польскую запись.
- [EvaluationResult.java](/Users/littlej/Projects/learning/scalc/src/main/java/local/myproject/calculate/evaluator/EvaluationResult.java) — содержит итоговый операнд и список атомарных действий.

### `calculate.formatter`

Форматирование выражений и операций.

- [ExpressionFormatter.java](/Users/littlej/Projects/learning/scalc/src/main/java/local/myproject/calculate/formatter/ExpressionFormatter.java) — преобразует внутренние модели в строковое представление.

### `calculate.operator`

Операторы и типы операторов/операндов.

- [Operators.java](/Users/littlej/Projects/learning/scalc/src/main/java/local/myproject/calculate/operator/Operators.java) — полный список поддерживаемых операторов и приоритетов.
- [BinaryOperators.java](/Users/littlej/Projects/learning/scalc/src/main/java/local/myproject/calculate/operator/BinaryOperators.java) — бинарные операции.
- [PrefixUnaryOperators.java](/Users/littlej/Projects/learning/scalc/src/main/java/local/myproject/calculate/operator/PrefixUnaryOperators.java) — префиксные унарные операции.
- [PostfixUnaryOperators.java](/Users/littlej/Projects/learning/scalc/src/main/java/local/myproject/calculate/operator/PostfixUnaryOperators.java) — постфиксные унарные операции.
- [Brackets.java](/Users/littlej/Projects/learning/scalc/src/main/java/local/myproject/calculate/operator/Brackets.java) — тип скобок.
- [TypeOfOperators.java](/Users/littlej/Projects/learning/scalc/src/main/java/local/myproject/calculate/operator/TypeOfOperators.java) — категории операторов.
- [TypeOfOperands.java](/Users/littlej/Projects/learning/scalc/src/main/java/local/myproject/calculate/operator/TypeOfOperands.java) — форматы операндов.
- [CustomMath.java](/Users/littlej/Projects/learning/scalc/src/main/java/local/myproject/calculate/operator/CustomMath.java) — дополнительные математические операции, например факториал.

### `calculate.converter`

Преобразование типов и форматов чисел.

- [Converter.java](/Users/littlej/Projects/learning/scalc/src/main/java/local/myproject/calculate/converter/Converter.java) — конвертация операндов и чисел.
- [Roman.java](/Users/littlej/Projects/learning/scalc/src/main/java/local/myproject/calculate/converter/Roman.java) — поддержка римских чисел.

### `calculate.validation`

Валидация строковых токенов.

- [Validator.java](/Users/littlej/Projects/learning/scalc/src/main/java/local/myproject/calculate/validation/Validator.java) — проверка чисел, операторов и определение типа операнда.

## Поток вычисления

Внутренний pipeline у движка сейчас выглядит так:

1. `Expression` принимает исходную строку.
2. `ExpressionNormalizer` приводит запись к нормализованному виду.
3. `UnitExpressionParser` разбивает строку на `Operand` и `Operator`.
4. `OperatorTypeResolver` уточняет контекстные типы операторов.
5. `PolishRecordBuilder` строит польскую запись.
6. `ExpressionEvaluator` вычисляет польскую запись.
7. `ExpressionFormatter` отдает строковое представление выражения, польской записи и атомарных шагов.

## Основной API фасада `Expression`

### Конструктор

```java
new Expression(String expression, String[]... parameters)
```

Параметры:

- `expression` — строковое выражение;
- `parameters` — массив пар `имя-значение`, например `new String[]{"A", "10"}`.

### Основные методы

- `normaliseExpr(String expression)` — нормализует запись выражения.
- `evaluate()` — вычисляет выражение и возвращает полный `EvaluationResult` с итоговым `Operand` и атомарными шагами.
- `calculate()` — короткий вызов, который возвращает только `Operand` и внутри использует `evaluate()`.
- `getUnitExpression()` — возвращает выражение в нормализованной форме.
- `getUnitExpression(TypeOfOperands type)` — возвращает выражение с выводом операндов в заданном формате.
- `getPolishRecord()` — возвращает польскую запись.
- `getPolishRecord(TypeOfOperands type)` — возвращает польскую запись в заданном формате операндов.
- `getAtomicActions()` — возвращает список атомарных действий в формате `выражение = результат`.
- `getAtomicActions(TypeOfOperands type)` — возвращает атомарные действия в указанном формате операндов.
- `countOfAtomicExpressions()` — возвращает число атомарных действий.
- `getAtomicExpression(int i)` — возвращает атомарное действие по индексу.
- `getAtomicExpression(int i, TypeOfOperands type)` — возвращает атомарное действие с заданным форматом вывода.

Важно:

- фасад `Expression` теперь stateless относительно результата вычисления;
- атомарные шаги не сохраняются во внутреннем изменяемом состоянии объекта;
- каждый вызов `evaluate()` возвращает новый immutable-результат расчета;
- `calculate()` использует уже подготовленную польскую запись, собранную в конструкторе.

## Поддерживаемые операторы

### Бинарные

- `+`
- `-`
- `*`
- `/`
- `^`

### Префиксные унарные

- `sin`
- `cos`
- `tan`
- `ctg`
- `ln`
- `lg`
- унарный `-`

### Постфиксные унарные

- `!`
- `%`

### Скобки

- `(`
- `)`

## Поддерживаемые типы операндов

Сейчас движок умеет работать с:

- `Arabic`
- `Roman`
- `Binary`

Также используется логический режим `Default`, который остается на уровне интеграции в `scalc` и означает: не форсировать отдельный тип вывода, а оставить вычисление в обычном режиме движка.

## Работа с параметрами

Движок умеет принимать параметры как пары `имя-значение`. Например:

```java
Expression expression = new Expression(
        "Total + Bonus",
        new String[]{"Total", "100"},
        new String[]{"Bonus", "25"}
);
```

Во время парсинга токен:

- сначала проверяется как число;
- затем как оператор;
- затем как параметр по имени.

Это позволяет использовать выражения проекта как переменные при интеграции в `scalc`.

## Атомарные действия

Атомарные действия теперь возвращаются как часть результата `evaluate()` и могут быть получены без мутации объекта `Expression`.

Например:

```java
Expression expression = new Expression("2 + 2 * 2");
EvaluationResult result = expression.evaluate();
```

Пример:

Для выражения `2 + 2 * 2`:

- атомарное действие `0`: `2 * 2`
- атомарное действие `1`: `2 + 4`

Именно это затем используется во внешнем API `scalc`, чтобы отдавать пользователю детальный список шагов вычисления.

## Интеграция в `scalc`

Прямой доступ к внутренним пакетам `calculate.*` из `domain` или `presentation` не рекомендуется.

Правильный путь интеграции сейчас такой:

- `application.expression.port.out.ExpressionCalculatorPort`
- [LegacyCalculationEngineAdapter.java](/Users/littlej/Projects/learning/scalc/src/main/java/local/myproject/scalc/infrastructure/calculation/LegacyCalculationEngineAdapter.java)
- `Expression`

То есть `calculate` выступает как engine-core, а приложение работает с ним через инфраструктурный адаптер.

## Тесты

Базовые проверки движка сейчас находятся в [ExpressionTest.java](/Users/littlej/Projects/learning/scalc/src/test/java/local/myproject/calculate/ExpressionTest.java).

Они проверяют:

- корректный расчет выражения;
- построение атомарных действий;
- подстановку параметров;
- поведение при выходе индекса атомарного действия за границы.

## Ограничения и особенности

Сейчас у движка есть несколько важных особенностей:

- фасад `Expression` уже stateless на уровне результатов вычисления, но сам по-прежнему остается фасадом над несколькими внутренними компонентами;
- часть имен и JavaDoc внутри legacy-классов еще нуждается в дополнительной чистке;
- package structure уже современнее, но не все типы одинаково хорошо скрыты как internal API;
- часть ошибок возвращается как старые `ArithmeticException` и `Exception`, без собственной иерархии engine-ошибок;
- `Expression` по-прежнему создает внутренние компоненты сам, без DI и без отдельного configuration-слоя, что нормально для engine-core, но ограничивает гибкость.

## Что логично делать дальше

Если развивать пакет дальше, самые полезные следующие шаги такие:

1. Добавить `package-info.java` в каждый подпакет `calculate`.
2. Дочистить JavaDoc и нейминг внутри `operator`, `converter`, `model`.
3. Постепенно скрыть внутренние классы там, где они не должны быть внешним API.
4. Ввести собственные типы исключений для ошибок парсинга и вычисления.
5. Добавить больше unit-тестов на:
   - унарный минус;
   - римские числа;
   - бинарные числа;
   - ошибки `Infinity` и `NaN`;
   - неявное умножение.

## Краткий итог

`calculate` сейчас — это не случайный набор legacy-классов, а уже структурированный внутренний вычислительный движок с фасадом `Expression`, техническими подпакетами и интеграцией в архитектуру `scalc` через отдельный инфраструктурный адаптер.

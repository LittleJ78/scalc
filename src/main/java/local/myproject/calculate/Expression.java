package local.myproject.calculate;

import local.myproject.calculate.converter.Converter;
import local.myproject.calculate.evaluator.AtomicExpressionStep;
import local.myproject.calculate.evaluator.EvaluationResult;
import local.myproject.calculate.evaluator.ExpressionEvaluator;
import local.myproject.calculate.formatter.ExpressionFormatter;
import local.myproject.calculate.model.Operand;
import local.myproject.calculate.model.Unit;
import local.myproject.calculate.operator.TypeOfOperands;
import local.myproject.calculate.parser.ExpressionNormalizer;
import local.myproject.calculate.parser.OperatorTypeResolver;
import local.myproject.calculate.parser.PolishRecordBuilder;
import local.myproject.calculate.parser.UnitExpressionParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Deque;
import java.util.List;

/**
 * Фасад вычислительного движка для работы с алгебраическим выражением.
 *
 * @author Evgenii Mironov
 */
public class Expression {
	private static final Logger logger = LoggerFactory.getLogger(Expression.class.getName());
	/** Контейнер для хранения алгебраического выражения в виде польской записи. */
	private final Deque<Unit> polishRecord;
	/** Контейнер для хранения алгебраического выражения в виде простой записи. */
	private final List<Unit> unitExpression;
	private final ExpressionNormalizer expressionNormalizer = new ExpressionNormalizer();
	private final UnitExpressionParser unitExpressionParser = new UnitExpressionParser();
	private final OperatorTypeResolver operatorTypeResolver = new OperatorTypeResolver();
	private final PolishRecordBuilder polishRecordBuilder = new PolishRecordBuilder();
	private final ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator();
	private final ExpressionFormatter expressionFormatter = new ExpressionFormatter();


	/**
	 * Создает выражение и подготавливает его внутренние представления.
	 *
	 * @param expression алгебраическое выражение вида "1 + 2 - (3 - 1)"
	 * @param parameter набор параметров для подстановки
	 * @throws Exception если выражение не удалось разобрать
	 */
	public Expression(String expression, String[]... parameter) throws Exception {
		List<Unit> parsedExpression = unitExpressionParser.parse(expression, parameter);
		logger.info("записали нормализованую запись {} ", expressionFormatter.format(parsedExpression, null));
		this.unitExpression = operatorTypeResolver.resolve(parsedExpression);
		logger.info("переопределили операторы в выражении {} ", this.getUnitExpression());
		this.polishRecord = polishRecordBuilder.build(unitExpression);
		logger.info("Записали польскую запись {}", getPolishRecord());
	}

	/**
	 * Нормализует запись выражения, добавляя пробелы вокруг операторов.
	 *
	 * @param expression алгебраическое выражение
	 * @return нормализованное алгебраическое выражение
	 */
	public String normaliseExpr(String expression) {
    	return expressionNormalizer.normalize(expression);
	}

	/**
	 * Вычисляет выражение и возвращает полный результат расчета без изменения состояния фасада.
	 *
	 * @return полный результат вычисления
	 */
	public EvaluationResult evaluate() {
		return expressionEvaluator.evaluate(polishRecord);
	}

	/**
	 * Вычисляет результат польской записи.
	 *
 	 * @return результат в виде операнда
	 */
	public Operand calculate() throws ArithmeticException {
		return evaluate().operand();
	}
	/**
	 * Возвращает алгебраическое выражение в виде строки.
	 *
	 * @return алгебраическое выражение
	 */
	public String getUnitExpression() {
		return expressionFormatter.format(unitExpression, null);
	}
	/**
	 * Возвращает алгебраическое выражение в указанном формате операндов.
	 *
	 * @param type тип, в котором нужно вывести операнд
	 * @return алгебраическое выражение
	 */
	public String getUnitExpression(TypeOfOperands type) {
		return expressionFormatter.format(unitExpression, type);
	}
	/**
	 * Возвращает польскую запись в виде строки.
	 *
	 * @return польская запись
	 */
	public String getPolishRecord() {
		return expressionFormatter.format(polishRecord, null);
	}
	/**
	 * Возвращает польскую запись в указанном формате операндов.
	 *
	 * @param type тип, в котором нужно вывести операнд
	 * @return польская запись
	 */
	public String getPolishRecord(TypeOfOperands type) {
		return expressionFormatter.format(polishRecord, type);
	}
	/**
	 * Возвращает количество атомарных выражений.
	 *
	 * @return количество атомарных выражений
	 */
	public int countOfAtomicExpressions() {
		return evaluate().atomicExpressions().size();
	}

	/**
	 * Возвращает атомарное алгебраическое выражение по номеру операции.
	 *
	 * @param i номер атомарного выражения
	 * @return запрошенное выражение в виде строки
	 */
	public String getAtomicExpression(int i) throws Exception{
		AtomicExpressionStep step = getAtomicStep(i);
		return expressionFormatter.formatAtomic(step.expression(), null);
	}
	/**
	 * Возвращает атомарное выражение по номеру операции в указанном формате операндов.
	 *
	 * @param i номер атомарного выражения
	 * @param type тип, в котором нужно вывести операнд
	 * @return запрошенное выражение в виде строки
	 */
	public String getAtomicExpression(int i, TypeOfOperands type) throws Exception{
		AtomicExpressionStep step = getAtomicStep(i);
		return expressionFormatter.formatAtomic(step.expression(), type);
	}

	/**
	 * Возвращает список атомарных действий с результатом каждого шага.
	 *
	 * @return список атомарных действий
	 */
	public List<String> getAtomicActions() {
		return getAtomicActions(evaluate(), null);
	}

	/**
	 * Возвращает список атомарных действий с результатом каждого шага в указанном формате операндов.
	 *
	 * @param type тип, в котором нужно вывести операнды
	 * @return список атомарных действий
	 */
	public List<String> getAtomicActions(TypeOfOperands type) {
		return getAtomicActions(evaluate(), type);
	}

	/**
	 * Возвращает список атомарных действий для уже вычисленного результата.
	 *
	 * @param evaluationResult результат вычисления выражения
	 * @return список атомарных действий
	 */
	public List<String> getAtomicActions(EvaluationResult evaluationResult) {
		return getAtomicActions(evaluationResult, null);
	}

	/**
	 * Возвращает список атомарных действий для уже вычисленного результата в указанном формате операндов.
	 *
	 * @param evaluationResult результат вычисления выражения
	 * @param type тип, в котором нужно вывести операнды
	 * @return список атомарных действий
	 */
	public List<String> getAtomicActions(EvaluationResult evaluationResult, TypeOfOperands type) {
		return evaluationResult.atomicExpressions().stream()
				.map(step -> formatAtomicAction(step, type))
				.toList();
	}

	/**
	 * Возвращает атомарный шаг по индексу.
	 *
	 * @param i номер атомарного выражения
	 * @return атомарный шаг вычисления
	 */
	private AtomicExpressionStep getAtomicStep(int i) {
		List<AtomicExpressionStep> atomicExpressions = evaluate().atomicExpressions();
		if( i < 0 || i >= atomicExpressions.size()) {
			throw new IndexOutOfBoundsException(String.format("количество операций %d, запрошено %d", atomicExpressions.size(), i));
		}
		return atomicExpressions.get(i);
	}

	/**
	 * Форматирует атомарное действие с результатом шага.
	 *
	 * @param step атомарный шаг вычисления
	 * @param type тип, в котором нужно вывести операнды
	 * @return строковое представление атомарного действия
	 */
	private String formatAtomicAction(AtomicExpressionStep step, TypeOfOperands type) {
		String formattedExpression = expressionFormatter.formatAtomic(step.expression(), type);
		String formattedResult = type != null
				? Converter.operandToString(step.result(), type)
				: Converter.operandToString(step.result());
		return formattedExpression + " = " + formattedResult;
	}
}

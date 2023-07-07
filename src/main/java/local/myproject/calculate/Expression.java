package local.myproject.calculate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс для хранения алгебраического выражения в разных видах
 * @author Evgenii Mironov
 * version 1.0
 */
public class Expression {
	private static final Logger logger = LoggerFactory.getLogger(Expression.class.getName());
	/**контейнер для хранения алгебраического выражения в виде польской записи*/
	private Deque<Unit> polishRecord;
	/**контейнер для хранения алгебраического выражения в виде простой записи*/
	private List<Unit> unitExpression;
	/**контейнер для хранения алгебраического выражения разбитого на отдельные операции */
	private List<List<Unit>> atomicExpression;
	/**масив для хранения переданных параметоров и их значений*/
	private String[][] parameter;


	/**
	 * конструктор
	 * @param expression - Алгебраическое выражение вида "1 + 2 - (3 - 1)"
	 * @see Operators - перечисление поддерживаемых операторов
	 * @see local.myproject.calculate.TypeOfOperands - перечисление поддерживаемых форматов чисел
	 */
	public Expression(String expression, String[]... parameter) throws Exception {
		this.parameter = parameter;
		this.unitExpression = toUnitExpression(expression);
		logger.info("записали нормализованую запись {} ",this.getUnitExpression());
		this.unitExpression = normaliseOperatorsTypeInUnitExpr(unitExpression);
		logger.info("переопределили операторы в выражении {} ",this.getUnitExpression());
		this.polishRecord = toPolishRecord(expression);
		logger.info("Записали польскую запись " + getPolishRecord());
	}

	/**
	 * переопределение операторов с изменяемым типом - минус может быть как бинарным так и префиксным унарным -
	 * в зависимости от места где  оператор находиться в алгебраическом выражении
	 * @param expression - алгебраическое выражение в виде списка из отдельных операторов и операнд
	 * @return - алгебраическое выражение в виде списка из отдельных переопределенных операторов и операнд
	 */
	private List<Unit> normaliseOperatorsTypeInUnitExpr (List<Unit> expression) throws Exception {
		if (Operator.class.isInstance(expression.get(0)) && expression.get(0).getValue().equals("-")) {
			logger.trace("обрабатываем {}",expression.get(0).getValue());
			((Operator) expression.get(0)).setTypeOfOperator(TypeOfOperators.PrefixUnaryOperators);
			logger.trace("изменили тип оператора с индексом 0 на {} ", TypeOfOperators.PrefixUnaryOperators);
		}
		for (int i = 1; i < expression.size(); i++) {
			logger.trace("обрабатываем {}",expression.get(i).getValue());
			if(Operator.class.isInstance(expression.get(i)) && expression.get(i).getValue().equals("-")) {
				if(Operator.class.isInstance(expression.get(i - 1)) && !expression.get(i-1).getValue().equals(")")
				&& !expression.get(i - 1).getType().equals(TypeOfOperators.PostfixUnaryOperators)) {
					((Operator) expression.get(i)).setTypeOfOperator(TypeOfOperators.PrefixUnaryOperators);
					logger.trace("изменили тип оператора с индексом {} на {} ",i , TypeOfOperators.PrefixUnaryOperators);
				}
			}
		}
		return expression;
	}

	/**
	 * преобразование сроки в контейнер - простая запись
	 * @param expression - Алгебраическое выражение вида "1 + 2 - (3 - 1)"
	 * @return - алгебраическое выражение в виде списка из отдельных операторов и операнд
	 * @see Unit - абстрактный класс для операторов и операнд
	 */
	private List<Unit> toUnitExpression(String expression) {
		String str = normaliseExpr(expression);
		logger.debug("нормализовали запись {}", str);
		List<String> stringExpression = Arrays.stream(str.split(" ")).toList();

		Deque<Unit> buffer = new LinkedList<>();
		logger.trace("ОБРАБОТКА НОРМАЛИЗОВАНОЙ ЗАПИСИ, [{}] элемента - {}",stringExpression.size(), stringExpression);
		for(int i = 0; i < stringExpression.size(); i++) {
			logger.trace("обрабатывается элемент {}", stringExpression.get(i));
			if (local.myproject.calculate.Validator.validateNumber(stringExpression.get(i))) {
				buffer.add(new local.myproject.calculate.Operand(local.myproject.calculate.Validator.setTypeOfOperand(stringExpression.get(i)), local.myproject.calculate.Converter.anyTypeNumbToDouble(stringExpression.get(i)), null));
				logger.trace("добавили {} в контейнер", stringExpression.get(i));
			}
			if (local.myproject.calculate.Validator.validateOperation(stringExpression.get(i))) {
				for (Operators operator : Operators.values()) {
					if (operator.getOperator().equals(stringExpression.get(i))) {
						buffer.add(new Operator(operator));
						logger.trace("добавили {} в контейнер", stringExpression.get(i));
					}
				}
			}
			if (parameter != null) {
				for (int num = 0; num < parameter.length; num++) {
					if (parameter[num][0].equals(stringExpression.get(i)) && local.myproject.calculate.Validator.validateNumber(parameter[num][1])) {
						buffer.add(new local.myproject.calculate.Operand(Validator.setTypeOfOperand(parameter[num][1]), local.myproject.calculate.Converter.anyTypeNumbToDouble(parameter[num][1]), parameter[num][0]));
					}
				}
			}
		}

		Deque <Unit> result = new LinkedList<>();
		while(!buffer.isEmpty()){  //преобразование "1(2-1)1" в "1 * (2 - 1) * 1"
			if(Operator.class.isInstance(buffer.peekLast()) && !result.isEmpty())
				if (buffer.peekLast().getValue().equals(")")
						&& (local.myproject.calculate.Operand.class.isInstance(result.peekFirst())
						|| result.peekFirst().getValue().equals("(")
						|| result.peekFirst().getType().equals(TypeOfOperators.PrefixUnaryOperators))){
					result.addFirst(new Operator(Operators.Multiplication));
				}
			if(!result.isEmpty() && Operator.class.isInstance(result.peekFirst()))
				if(result.peekFirst().getValue().equals("(")
						&& local.myproject.calculate.Operand.class.isInstance(buffer.peekLast())) {
					result.addFirst(new Operator(Operators.Multiplication));
				}
			result.addFirst(buffer.pollLast());
		}
		return (List<Unit>) result;
	}

	/**
	 * метод нормализует запись -
	 * убирает лишние пробелы и добавляет нужные -
	 * "1-2 +(3  *4)" -> "1 - 2 + ( 3 * 4 )"
	 * @param expression - алгебраическое выражение
	 * @return - нормализованое алгебраическое выражение
	 */
	public String normaliseExpr(String expression) {
    	expression = expression.replaceAll(" ", "");
    	for (Operators operator : Operators.values()) {
    		expression = expression.replace(operator.getOperator(), " " + operator.getOperator() + " ");
    	}
		expression = expression.replaceAll("  ", " ").trim();
    	return expression;
	}

	/**
	 * преобразование алгебраического выражения в польскую запись
	 * @param expression - алгебраическое выражение
	 * @return - польская запись
	 */
	private Deque<Unit> toPolishRecord(String expression) {

		Deque <Unit> stack = new LinkedList<>();
		Deque<Unit> result = new LinkedList<>();


		for(int i = 0; i < unitExpression.size(); i++) {

			if(local.myproject.calculate.Operand.class.isInstance(unitExpression.get(i))) {
				result.add(unitExpression.get(i));
				logger.trace("добавили в запись {}", result.peekLast().getValue());
			}
			if(Operator.class.isInstance(unitExpression.get(i))) {
				Operator curentOperator = (Operator) unitExpression.get(i);
				for (Operators operator : Operators.values()) {
					if(operator.getOperator().equals( curentOperator.getValue()) ) {
						if(curentOperator.getType().equals(TypeOfOperators.BinaryOperators)) {
							while(!stack.isEmpty() && ( (stack.peekLast().getType().equals(TypeOfOperators.PrefixUnaryOperators)
									|| stack.peekLast().getPriority() >= curentOperator.getPriority())
									&& !stack.peekLast().getType().equals(TypeOfOperators.Brackets)) ) {
								result.add(stack.pollLast());
								logger.trace("добавили из стека в запись {}", result.peekLast().getValue());
							}
						}
						if(curentOperator.getValue().equals(")")) {
							while (!stack.peekLast().getValue().equals("(")) {
								result.add(stack.pollLast());
								logger.trace("добавили из стека в запись между скобками {}", result.peekLast().getValue());
							}
							logger.trace("убрали из стека {}", stack.peekLast().getValue());
							stack.pollLast();
						}
						else {
							stack.addLast(curentOperator);
							logger.trace("добавили в стек {}", stack.peekLast().getValue());
						}
					}
				}
			}
		}
		while(stack.size() > 0) {
			result.add(stack.pollLast());
			logger.trace("добавили из стека в запись {}", result.peekLast().getValue());
		}
		return result;
	}

	/**
	 * вычислеие результата польской записи
 	 * @return - результат в виде операнда
	 * @see local.myproject.calculate.Operand
	 */
	public local.myproject.calculate.Operand calculate() throws ArithmeticException {
		Deque <Unit> stack = new LinkedList<>();
		Deque<Unit> polishRecord = new LinkedList<>(this.polishRecord);
		atomicExpression = new ArrayList<>();

		while (!polishRecord.isEmpty()) {
			if (local.myproject.calculate.Operand.class.isInstance(polishRecord.peekFirst())) {
				stack.addLast(polishRecord.pollFirst());
			}
			if (Operator.class.isInstance(polishRecord.peekFirst())) {
				local.myproject.calculate.Operand secondOperand = (local.myproject.calculate.Operand) stack.pollLast();
				Operator operator = (Operator) polishRecord.pollFirst();
				logger.trace("выполняется операция \" {} \" ",operator.getValue());
				if (operator.getType().equals(TypeOfOperators.BinaryOperators)) {
					logger.trace("выполняется бинарная операция");
					local.myproject.calculate.Operand firstOperand = (local.myproject.calculate.Operand) stack.pollLast();
					stack.addLast(firstOperand.apply(operator, secondOperand));
					atomicExpression.add(new ArrayList<>(List.of(firstOperand,operator,secondOperand)));
					switch (stack.peekLast().getValue()) {
						case "Infinity":
							logger.warn("{} {} {} = {} ", local.myproject.calculate.Converter.operandToString(firstOperand),operator.getValue(), local.myproject.calculate.Converter.operandToString(secondOperand), local.myproject.calculate.Converter.operandToString((local.myproject.calculate.Operand) stack.peekLast()));
							throw new ArithmeticException("Infinity");
						case "NaN":
							logger.warn("{} {} {} = {} ", local.myproject.calculate.Converter.operandToString(firstOperand),operator.getValue(), local.myproject.calculate.Converter.operandToString(secondOperand), local.myproject.calculate.Converter.operandToString((local.myproject.calculate.Operand) stack.peekLast()));
							throw new ArithmeticException("Not-a-Number");
					}
					logger.debug("{} {} {} = {} ", local.myproject.calculate.Converter.operandToString(firstOperand),operator.getValue(), local.myproject.calculate.Converter.operandToString(secondOperand), local.myproject.calculate.Converter.operandToString((local.myproject.calculate.Operand) stack.peekLast()));
				}
				else {
					logger.trace("выполняется унарная операция");
					stack.addLast(secondOperand.apply(operator));
					if(operator.getType().equals(TypeOfOperators.PrefixUnaryOperators)) {
						atomicExpression.add(new ArrayList<>(List.of(operator, secondOperand)));
					} else if (operator.getType().equals(TypeOfOperators.PostfixUnaryOperators)) {
						atomicExpression.add(new ArrayList<>(List.of(secondOperand, operator)));
					}
					switch (stack.peekLast().getValue()) {
						case "Infinity":
							logger.error("{} {} = {} ", operator.getValue(), local.myproject.calculate.Converter.operandToString(secondOperand), local.myproject.calculate.Converter.operandToString((local.myproject.calculate.Operand) stack.peekLast()));
							throw new ArithmeticException("Infinity");
						case "NaN":
							logger.error("{} {} = {} ", operator.getValue(), local.myproject.calculate.Converter.operandToString(secondOperand), local.myproject.calculate.Converter.operandToString((local.myproject.calculate.Operand) stack.peekLast()));
							throw new ArithmeticException("Not-a-Number");
					}
					logger.debug("{} {} = {} ", operator.getValue(), local.myproject.calculate.Converter.operandToString(secondOperand), local.myproject.calculate.Converter.operandToString((local.myproject.calculate.Operand) stack.peekLast()));
				}
			}
		}
		if (stack.size() != 1) {
			logger.error("Величина стека = {}, стек {}",stack.size(), getExpression((List<Unit>) stack, null));
			throw new ArithmeticException("Ошибка вычисления выражения");
		}
		return (local.myproject.calculate.Operand) stack.pollFirst();
	}
	/**
	 * возвращает переданое выражение в виде строки
	 * @param expression - выражение в виде листа обьектов с интерфейсом Unit
	 * @param type - тип в котором нужно вывести операнд в переданном выражении, если null, то значение берется
	 *             из операнда (определенное по умолчаню во время ввода операнада)
	 * @return - алгебраическое выражение
	 */
	private  String getExpression(Collection<Unit> expression, local.myproject.calculate.TypeOfOperands type) {
		String result = expression.stream()
				.map(x -> local.myproject.calculate.Operand.class.isInstance(x) ?
						  ((local.myproject.calculate.Operand) x).getName().equals("DefaultName") ?
								  type != null ? local.myproject.calculate.Converter.operandToString((local.myproject.calculate.Operand) x, type) : local.myproject.calculate.Converter.operandToString((local.myproject.calculate.Operand) x)
								  : ((local.myproject.calculate.Operand) x).getName()
						  : x.getValue())
				.collect(Collectors.joining(" "));
		return result;
	}
	/**
	 * возвращает алгебраическое выражение в виде строки
	 * @return - алгебраическое выражение
	 */
	public String getUnitExpression() {
		return getExpression(unitExpression, null);
	}
	/**
	 * перегруженый метод, возвращает алгебраическое выражение в виде строки
	 * @param type - тип в котором нужно вывести операнд в алгебраическом выражении
	 * @return - алгебраическое выражение
	 */
	public String getUnitExpression(local.myproject.calculate.TypeOfOperands type) {
		return getExpression(unitExpression, type);
	}
	/**
	 * возвращает польскую запись в виде строки
	 * @return - польская запись
	 */
	public String getPolishRecord() {
		return getExpression(polishRecord, null);
	}
	/**
	 * перегруженый метод, возвращает польскую запись в виде строки
	 * @param type - тип в котором нужно вывести операнд в польской записи
	 * @return - польская запись
	 */
	public String getPolishRecord(local.myproject.calculate.TypeOfOperands type) {
		return getExpression(polishRecord, type);
	}
	/**
	 * гетер
	 * @return - возвращает количество атомарных выражений
	 */
	public int countOfAtomicExpressions() {
		return atomicExpression.size();
	}

	/**
	 * возвращает атомарноге алгебраическоге выражение по указаному номеру операции
	 * @param i - номер атомарного алгебраического выражения (номеру операции польской записи)
	 * @return - запрошеное выражение в виде строки
	 */
	public String getAtomicExpression(int i) throws Exception{
		if( i < 0 || i >= atomicExpression.size()) {
			throw new IndexOutOfBoundsException(String.format("количество операций %d, запрошено %d", atomicExpression.size(), i));
		}
		int size = atomicExpression.get(i).size();
		StringBuilder expression = new StringBuilder();
		for (int j = 0; j < size; j++) {
			expression.append( local.myproject.calculate.Operand.class.isInstance(atomicExpression.get(i).get(j)) ?
					local.myproject.calculate.Converter.operandToString((local.myproject.calculate.Operand) atomicExpression.get(i).get(j)) :
					atomicExpression.get(i).get(j).getValue()).append(" ");
		}
		return expression.toString().trim();
	}
	/**
	 * перегруженый метод, возвращает атомарноге алгебраическоге выражение по указаному номеру операции
	 * @param i - номер атомарного алгебраического выражения (номеру операции польской записи)
	 * @param type - тип в котором нужно вывести операнд атомарного алгебраического выражения
	 * @return - запрошеное выражение в виде строки
	 */
	public String getAtomicExpression(int i, TypeOfOperands type) throws Exception{
		if( i < 0 || i >= atomicExpression.size()) {
			throw new IndexOutOfBoundsException(String.format("количество операций %d, запрошено %d", atomicExpression.size(), i));
		}
		int size = atomicExpression.get(i).size();
		StringBuilder expression = new StringBuilder();
		for (int j = 0; j < size; j++) {
			expression.append( local.myproject.calculate.Operand.class.isInstance(atomicExpression.get(i).get(j)) ?
					local.myproject.calculate.Converter.operandToString((Operand) atomicExpression.get(i).get(j), type) :
					atomicExpression.get(i).get(j).getValue()).append(" ");
		}
		return expression.toString().trim();
	}


	public static void main(String[] args) throws Exception {
		String[][] par =  new String [3][2];
		par[0][0] = "A";
		par[0][1] = "-2";
		par[1][0] = "B";
		par[1][1] = "90";
		par[2][0] = "C1";
		par[2][1] = "33";
		Expression expression = new Expression("A+1+B-A",par);
		expression.calculate();
		for(int i = 0; i < expression.countOfAtomicExpressions(); i++) {
			logger.info("{}) {}",i + 1 ,expression.getAtomicExpression(i));
		}
		logger.info("{}", Converter.operandToString(expression.calculate()));
	}
}
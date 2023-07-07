package local.myproject.calculate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.stream.Collectors;
/**
 * Класс для операндов использующихся в алгебраическом выражении
 * @author Evgenii Mironov
 * version 1.0
 */
public class Operand extends Unit {

	private static final Logger logger = LoggerFactory.getLogger(Operand.class.getName());
	/** формат числа соотносящийся к типу системы счисления вкоторой записан операнд - арабское, римское, двоичное и т.д. */
	private local.myproject.calculate.TypeOfOperands type;
	/** имя операнда, имя по умолчанию DefaultName, используется для параметризации выражения*/
	private String name = "DefaultName";
	/** числовое значение операнда */
	private double value;


	/**
	 * конструктор
	 * @param type - формат числа соотносящийся к типу системы счисления вкоторой записан операнд
	 *                - арабское, римское, двоичное и т.д.
	 * @param value - числовое значение операнда
	 * @param name - имя операнда если он является параметризованым
	 */
	public Operand(local.myproject.calculate.TypeOfOperands type, double value, String name) {
		this.type = type;
		this.value = value;
		this.name = name != null ? name : "DefaultName";
	}

	/**
	 * перегруженый метод для вычисления унарного алгебраического выражения
	 * @param operation - унарная операция - синус, косинус и т.п.
	 * @return итоговый результат операции в виде нового операнда
	 */
	public Operand apply(Operator operation) {
		String strClass = this.getClass().getName().replaceAll("[a-zA-Z]*$","");
		String name = Arrays.stream(Operators.values())
				.filter(o -> operation.getValue().equals(o.getOperator()))
				.map(o -> o.name()).collect(Collectors.joining());

		logger.trace("обработка унарного оператора {}", name);
		try {
			Class classForCurrentUnaryOperators = Class.forName(strClass + operation.getType().name());
			logger.trace("ищем {} в классе {} ", name, classForCurrentUnaryOperators.getName());
		}
		catch (ClassNotFoundException ex) {
			logger.error("Didn't find Class for {}",strClass + operation.getType().name());
		}
		double result = 0;
		switch (operation.getType().name()) {
			case "PrefixUnaryOperators":
				result = PrefixUnaryOperators.valueOf(name).get().apply(this.value);
				break;
			case "PostfixUnaryOperators":
				result = PostfixUnaryOperators.valueOf(name).get().apply(this.value);
				break;
		}


		result = result < 0 && this.type == local.myproject.calculate.TypeOfOperands.Roman ? 0 : result;
		return new Operand(this.type, result,null);
	}

	/**
	 * перегруженый метод для вычисления бинарного алгебраического выражения
	 * @param operation - бинарная операция - плюс, минус и т.п.
	 * @param nextNumber - второй операнд
	 * @return
	 */
	public Operand apply(Operator operation, Operand nextNumber) {
		String name = Arrays.stream(Operators.values())
				.filter(o -> operation.getValue().equals(o.getOperator()))
				.map(o -> o.name()).collect(Collectors.joining());

				logger.trace("обработка бинарного оператора {}", name);
				double result = BinaryOperators.valueOf(name).get().apply(this.value, nextNumber.value);
		result = result < 0 && this.type == local.myproject.calculate.TypeOfOperands.Roman ? 0 : result;
		return new Operand(this.type, result, null);
	}

	/**
	 * гетер
	 * @return числовое значение операнда
	 */
	@Override
	public String getValue() {
		return Converter.doubleToString(value);
	}

	/**
	 * гетер
	 * @return формат числа соотносящийся к типу системы счисления вкоторой записан операнд
	 *                 - арабское, римское, двоичное и т.д.
	 */
	@Override
	public TypeOfOperands getType () {
		return type;
	}

	/**
	 * гетер
	 * @return - возвращает имя операнда, по умолчанию DefaultName, если возвращает другое, то операнд параметризован
	 */
	public String getName() {
		return name;
	}

	/**
	 * сетер
	 * @param name - имя операнда, задается если операнд является параметром
	 */
	public void setName(String name) {
		this.name = name;
	}

}



package local.myproject.calculate.validation;

import local.myproject.calculate.converter.Converter;
import local.myproject.calculate.operator.Operators;
import local.myproject.calculate.operator.TypeOfOperands;

/**
 * Утилита валидации операндов и операторов.
 *
 * @author Evgenii Mironov
 */
public class Validator {

	/**
	 * Скрывает создание экземпляра утилитного класса.
	 */
	private Validator() {
	}

	/**
	 * валидатор арабских чисел
	 * @param arabicNum число
	 * @return валиден/не валиден
	 */
	public static boolean validateArabic(String arabicNum) {
		return arabicNum.matches("^-?[0-9.]+");
	}

	/**
	 * Валидирует римские числа в диапазоне от I до MMMCMXCIX.
	 *
	 * @param romanNum римское число
	 * @return {@code true}, если число валидно
	 */
	public static boolean validateRoman(String romanNum) {
		boolean valid = romanNum.chars().mapToObj(x -> String.valueOf((char) x)).allMatch(x -> x.matches("[IVXLCD]"));
		return valid ? romanNum.equals(Converter.doubleToRoman(Converter.romanToDouble(romanNum))) : false;
	}

	/**
	 * валидатор двоичных чисел
	 * @param binaryNum число
	 * @return валиден/не валиден
	 */
	public static boolean validateBinary(String binaryNum) {
		return binaryNum.matches("^0b[01]+");
	}

	/**
	 * Выполняет общую проверку на числовой операнд.
	 *
	 * @param numb строковое представление числа
	 * @return {@code true}, если значение является поддерживаемым числом
	 */
	public static boolean validateNumber(String numb) {
		return validateArabic(numb) || validateRoman(numb) || validateBinary(numb);
	}
	/**
	 * Определяет тип операнда по его строковому представлению.
	 *
	 * @param numb строковое представление числа
	 * @return тип операнда
	 */
	public static TypeOfOperands setTypeOfOperand(String numb) {
		if(validateRoman(numb)) {
			return TypeOfOperands.Roman;
		}
		if (validateArabic(numb)) {
			return TypeOfOperands.Arabic;
		}
		if (validateBinary(numb)) {
			return TypeOfOperands.Binary;
		}

		return null;
	}

	/**
	 * валидатор оператора
	 * @param operation - оператор
	 * @return валиден/не валиден
	 */
	public static boolean validateOperation(String operation) {
		for (Operators operator : Operators.values()) {
			if (operation.equals(operator.getOperator())) {
				return true;
			}
		}
		return false;
	}

}

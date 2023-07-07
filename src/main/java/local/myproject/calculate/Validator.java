package local.myproject.calculate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * клас определяющий методы для валидации разных значений
 * @author Evgenii Mironov
 * version 1.0
 */
public class Validator {

	private static final Logger logger = LoggerFactory.getLogger(Validator.class.getName());

	/**
	 * конструктор, т.к. все метода класса должны быть статическими, конструктор приватный
	 */

	private Validator() {
	}

	/**
	 * валидатор арабских чисел
	 * @param arabicNum число
	 * @return валиден/не валиден
	 */
	static boolean validateArabic(String arabicNum) {
		return arabicNum.matches("^-?[0-9.]+");
	}

	/**
	 * валидатор римских чисел
	 * @param romanNum число должно быть в диапазоне от 1 до 3 999 (от I до MMMCMXCIX)
	 * @return валиден/не валиден
	 */
	static boolean validateRoman(String romanNum) {
		boolean valid = romanNum.chars().mapToObj(x -> String.valueOf((char) x)).allMatch(x -> x.matches("[IVXLCD]"));
		return valid ? romanNum.equals(local.myproject.calculate.Converter.doubleToRoman(Converter.romanToDouble(romanNum))) : false;
	}

	/**
	 * валидатор двоичных чисел
	 * @param binaryNum число
	 * @return валиден/не валиден
	 */
	static boolean validateBinary(String binaryNum) {
		return binaryNum.matches("^0b[01]+");
	}

	/**
	 * общий валидатор для чисел
	 * @return true если число
	 */
	public static boolean validateNumber(String numb) {
		return validateArabic(numb) || validateRoman(numb) || validateBinary(numb);
	}
	/**
	 * валидатор для типа числа - арабсрое / римское и т.п.
	 */
	static local.myproject.calculate.TypeOfOperands setTypeOfOperand(String numb) {
		if(validateRoman(numb)) {
			return local.myproject.calculate.TypeOfOperands.Roman;
		}
		if (validateArabic(numb)) {
			return local.myproject.calculate.TypeOfOperands.Arabic;
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
	static boolean validateOperation(String operation) {
		for (Operators operator : Operators.values()) {
			if (operation.equals(operator.getOperator())) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		logger.info("{}",validateBinary("0b110010010111"));
	}
}
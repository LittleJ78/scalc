package local.myproject.scalc.domain.aggregate.expression;

import jakarta.validation.constraints.NotBlank;
import local.myproject.scalc.domain.aggregate.project.Project;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Доменная модель единицы выражения и операций над ней.
 *
 * @author Evgenii Mironov
 */
@Slf4j
@Getter
@Setter
@NoArgsConstructor
public class ExpressionUnit {
    private static final Set<String> RESERVED_NAMES = Set.of(
            "+", "-", "*", "/", "^", "%", "!", "(", ")",
            "sin", "cos", "tan", "ctg", "ln", "lg"
    );

    private int expressionUnitId;
    private String typeOfOperand;
    private String expressionUnitName;

    @NotBlank(message = "Expression can not be blank")
    private String defaultExpression = "";

    private String expressionResult;
    private ParametrisedExpressions parametrisedExpressions;
    private Project project;
    private boolean watchList;
    private String unitExpression;
    private List<String> atomicExpressions = new ArrayList<>();

    /**
     * Создает выражение с указанным типом операнда.
     *
     * @param typeOfOperand тип операнда
     * @return результат не возвращается
     */
    public ExpressionUnit(String typeOfOperand) {
        this.typeOfOperand = typeOfOperand == null ? "Default" : typeOfOperand;
    }

    /**
     * Устанавливает и нормализует исходное выражение.
     *
     * @param defaultExpression исходное выражение
     * @return результат не возвращается
     * @throws Exception если выражение не удалось нормализовать
     */
    public void setDefaultExpression(String defaultExpression) {
        log.info("setDefaultExpression <{}>", defaultExpression);
        this.defaultExpression = defaultExpression == null ? "" : defaultExpression;
    }

    /**
     * Возвращает список доступных типов операндов.
     *
     * @param args параметры не передаются
     * @return список типов операндов
     */
    public List<String> getTypesOfOperands() {
        return new ArrayList<>(Arrays.asList("Default", "Arabic", "Roman", "Binary"));
    }

    /**
     * Устанавливает имя выражения.
     * Выполняет проверку на совпадения с ключевыми словами и числами,
     * а затем приводит имя к формату `Имя_Имя`.
     * Если имя конфликтует с оператором или числом, к нему добавляется суффикс `value`.
     *
     * @param expressionUnitName исходное имя выражения
     * @return результат не возвращается
     */
    public void setExpressionUnitName(String expressionUnitName) {
        if (expressionUnitName == null || expressionUnitName.isBlank()) {
            this.expressionUnitName = expressionUnitName;
            return;
        }
        String operator = RESERVED_NAMES.contains(expressionUnitName.toLowerCase())
                ? expressionUnitName + " value"
                : expressionUnitName;
        operator = operator.matches("[-+]?\\d+(\\.\\d+)?") ? operator + " value" : operator;
        String[] name = operator.toLowerCase().replaceAll("_", " ").split("\\s+");
        StringBuilder newName = new StringBuilder();
        for (int i = 0; i < name.length; i++) {
            StringBuilder newNameBuf = new StringBuilder(name[i]);
            newNameBuf.setCharAt(0, Character.toUpperCase(newNameBuf.charAt(0)));
            if (i == 0) {
                newName.append(newNameBuf);
            } else {
                newName.append("_").append(newNameBuf);
            }
        }
        this.expressionUnitName = newName.toString();
    }
}

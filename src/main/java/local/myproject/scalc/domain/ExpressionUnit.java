package local.myproject.scalc.domain;

import jakarta.validation.constraints.NotBlank;
import local.myproject.calculate.Converter;
import local.myproject.calculate.Expression;
import local.myproject.calculate.Operators;
import local.myproject.calculate.TypeOfOperands;
import local.myproject.calculate.Validator;
import local.myproject.scalc.services.ExpressionUnitServiceImpl;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
public class ExpressionUnit {
    private int expressionUnitId;
    private String typeOfOperand;
    private String expressionUnitName;

    @NotBlank(message = "Expression can not be blank")
    private String defaultExpression = "";

    private String expressionResult;
    private ParametrisedExpressions parametrisedExpressions;
    private Project project;
    private boolean watchList;

    public ExpressionUnit(String typeOfOperand) {
        this.typeOfOperand = typeOfOperand == null ? "Default" : typeOfOperand;
    }

    public void setDefaultExpression(String defaultExpression) throws Exception {
        log.info("setDefaultExpression <{}>", defaultExpression);
        this.defaultExpression = defaultExpression;
        Expression expression = getExpression();
        if (!expression.getUnitExpression().equals("0")) {
            this.defaultExpression = expression.normaliseExpr(defaultExpression);
        }
    }

    public List<String> getTypesOfOperands() {
        List<String> typesOfOperands = new ArrayList<>();
        typesOfOperands.add("Default");
        typesOfOperands.addAll(Arrays.stream(TypeOfOperands.values()).map(Enum::name).toList());
        return typesOfOperands;
    }

    public Expression getExpression() throws Exception {
        if (!defaultExpression.isBlank()) {
            return typeOfOperand.equals("Default")
                    ? new Expression(defaultExpression, ExpressionUnitServiceImpl.arrayOfParameters)
                    : new Expression(
                            new Expression(defaultExpression, ExpressionUnitServiceImpl.arrayOfParameters)
                                    .getUnitExpression(TypeOfOperands.valueOf(typeOfOperand)),
                            ExpressionUnitServiceImpl.arrayOfParameters
                    );
        }
        return new Expression("0");
    }

    public List<String> getAtomicExpressions() throws Exception {
        List<String> atomicExpressions = new ArrayList<>();
        Expression expression = getExpression();
        expression.calculate();
        for (int i = 0; i < expression.countOfAtomicExpressions(); i++) {
            atomicExpressions.add(
                    expression.getAtomicExpression(i) + " = "
                            + Converter.operandToString(new Expression(
                            expression.getAtomicExpression(i).replaceAll("Zero", "I-I")
                    ).calculate())
            );
        }
        return atomicExpressions;
    }

    public String getUnitExpression() throws Exception {
        Expression expression = getExpression();
        this.expressionResult = Converter.operandToString(expression.calculate());
        return expression.getUnitExpression().equals("0")
                ? ""
                : expression.getUnitExpression() + " = " + expressionResult;
    }

    /**
     * Устанавливает имя выражения.
     * Выполняет проверку на совпадения с ключевыми словами и числами,
     * а затем приводит имя к формату `Имя_Имя`.
     * Если имя конфликтует с оператором или числом, к нему добавляется суффикс `value`.
     */
    public void setExpressionUnitName(String expressionUnitName) {
        String operator = Arrays.stream(Operators.values())
                .map(x -> Operators.valueOf(x.name()).getOperator().toLowerCase())
                .filter(x -> x.equals(expressionUnitName.toLowerCase()))
                .findFirst()
                .orElse("");
        operator = operator.isEmpty() ? expressionUnitName : expressionUnitName + " value";
        operator = Validator.validateNumber(operator.toUpperCase()) || Validator.validateNumber(operator.toLowerCase())
                ? operator + " value"
                : operator;
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

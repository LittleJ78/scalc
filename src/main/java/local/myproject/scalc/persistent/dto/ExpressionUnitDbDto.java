package local.myproject.scalc.persistent.dto;

import lombok.Data;

@Data
public class ExpressionUnitDbDto {
    private Integer expressionUnitId;
    private String typeOfOperand;
    private String expressionUnitName;
    private String defaultExpression;
    private String expressionResult;
    private Integer projectId;
    private boolean watchList;
}

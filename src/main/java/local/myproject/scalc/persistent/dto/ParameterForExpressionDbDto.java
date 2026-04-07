package local.myproject.scalc.persistent.dto;

import lombok.Data;

@Data
public class ParameterForExpressionDbDto {
    private Integer parametersForExpressionsId;
    private String parameter;
    private Integer parametrisedExpressionId;
}

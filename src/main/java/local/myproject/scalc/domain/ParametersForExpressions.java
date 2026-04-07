package local.myproject.scalc.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ParametersForExpressions {
    private int parametersForExpressionsId;
    private String parameter;
    private ParametrisedExpressions parametrisedExpressions;
}

package local.myproject.scalc.domain.aggregate.expression;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Доменная модель параметра, используемого в выражении.
 *
 * @author Evgenii Mironov
 */
@Getter
@Setter
@NoArgsConstructor
public class ParametersForExpressions {
    private int parametersForExpressionsId;
    private String parameter;
    private ParametrisedExpressions parametrisedExpressions;
}

package local.myproject.scalc.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ParametrisedExpressions {
    private int parametrisedExpressionId;
    private ExpressionUnit expressionUnit;
    private Set<ParametersForExpressions> parametersForExpressions = new HashSet<>();

    public void addParameter(ParametersForExpressions parameter) {
        parametersForExpressions.add(parameter);
    }

    public void clearParameter() {
        parametersForExpressions.clear();
    }

    public void addParameters(ParametersForExpressions... parameter) {
        for (ParametersForExpressions p : parameter) {
            p.setParametrisedExpressions(this);
            parametersForExpressions.add(p);
        }
    }
}

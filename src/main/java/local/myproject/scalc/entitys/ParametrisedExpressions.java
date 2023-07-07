package local.myproject.scalc.entitys;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ParametrisedExpressions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int parametrisedExpressionId;

    @OneToOne
    @JoinColumn(name = "expression_unit_id")
    ExpressionUnit expressionUnit;
    @OneToMany(mappedBy = "parametrisedExpressions", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval=true)
    Set<ParametersForExpressions> parametersForExpressions = new HashSet<>();

    public void addParameter(ParametersForExpressions parameter){
        parametersForExpressions.add(parameter);
    }
    public void clearParameter() {
        parametersForExpressions.clear();
    }
    public void addParameters(ParametersForExpressions... parameter){
        for (ParametersForExpressions p : parameter) {
            p.setParametrisedExpressions(this);
            parametersForExpressions.add(p);
        }
    }
}

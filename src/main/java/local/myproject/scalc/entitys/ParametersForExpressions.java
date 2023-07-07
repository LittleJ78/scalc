package local.myproject.scalc.entitys;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ParametersForExpressions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int parametersForExpressionsId;
    private String parameter;
    @ManyToOne()
    @JoinColumn(name = "parametrised_expression_id")
    private ParametrisedExpressions parametrisedExpressions;
}

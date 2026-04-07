package local.myproject.scalc.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class Project {
    private int projectId;
    private String name;
    private String description;
    private Set<ExpressionUnit> expressionUnits;
    private User user;
}

package local.myproject.scalc.domain;

import local.myproject.scalc.domain.ExpressionUnit;
import local.myproject.scalc.domain.Project;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExpressionUnitTest {

    @Test
    void fromEntityCopiesExpressionUnitFields() throws Exception {
        Project project = new Project();
        project.setProjectId(42);

        ExpressionUnit expressionUnit = new ExpressionUnit();
        expressionUnit.setExpressionUnitId(10);
        expressionUnit.setTypeOfOperand("Default");
        expressionUnit.setExpressionUnitName("Total_Value");
        expressionUnit.setDefaultExpression("2 + 2");
        expressionUnit.setExpressionResult("4");
        expressionUnit.setWatchList(true);
        expressionUnit.setProject(project);

        assertEquals(10, expressionUnit.getExpressionUnitId());
        assertEquals("Default", expressionUnit.getTypeOfOperand());
        assertEquals("Total_Value", expressionUnit.getExpressionUnitName());
        assertEquals("2 + 2", expressionUnit.getDefaultExpression());
        assertEquals("4", expressionUnit.getExpressionResult());
        assertEquals(42, expressionUnit.getProject().getProjectId());
        assertTrue(expressionUnit.isWatchList());
    }

    @Test
    void expressionCanExistWithoutProject() {
        ExpressionUnit expressionUnit = new ExpressionUnit();
        expressionUnit.setExpressionUnitId(11);
        expressionUnit.setWatchList(false);

        assertEquals(11, expressionUnit.getExpressionUnitId());
        assertNull(expressionUnit.getProject());
        assertFalse(expressionUnit.isWatchList());
    }
}

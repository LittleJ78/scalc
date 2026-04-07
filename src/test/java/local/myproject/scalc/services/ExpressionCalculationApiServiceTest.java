package local.myproject.scalc.services;

import local.myproject.scalc.presentation.dto.ExpressionCalculationRequestDto;
import local.myproject.scalc.presentation.dto.ExpressionCalculationResponseDto;
import local.myproject.scalc.presentation.mapper.ExpressionCalculationPresentationMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ExpressionCalculationApiServiceTest {

    private final ExpressionCalculationApiService expressionCalculationApiService =
            new ExpressionCalculationApiService(new ExpressionCalculationPresentationMapper());

    @Test
    void calculateReturnsResultAndAtomicActions() {
        ExpressionCalculationRequestDto request = new ExpressionCalculationRequestDto();
        request.setExpression("2 + 2");
        request.setTypeOfOperand("Default");

        ExpressionCalculationResponseDto response = expressionCalculationApiService.calculate(request);

        assertEquals("2 + 2", response.getExpression());
        assertEquals("4", response.getResult());
        assertEquals("2 + 2 = 4", response.getUnitExpression());
        assertNotNull(response.getAtomicActions());
        assertFalse(response.getAtomicActions().isEmpty());
    }
}

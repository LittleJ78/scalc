package local.myproject.scalc.services;

import local.myproject.scalc.domain.ExpressionUnit;
import local.myproject.scalc.presentation.dto.ExpressionCalculationRequestDto;
import local.myproject.scalc.presentation.dto.ExpressionCalculationResponseDto;
import local.myproject.scalc.presentation.mapper.ExpressionCalculationPresentationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpressionCalculationApiService {
    private final ExpressionCalculationPresentationMapper expressionCalculationPresentationMapper;

    public ExpressionCalculationResponseDto calculate(ExpressionCalculationRequestDto request) {
        ExpressionUnit expressionUnit = expressionCalculationPresentationMapper.toDomain(request);
        return expressionCalculationPresentationMapper.toDto(expressionUnit);
    }
}

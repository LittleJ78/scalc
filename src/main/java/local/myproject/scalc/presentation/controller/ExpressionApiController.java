package local.myproject.scalc.presentation.controller;

import local.myproject.scalc.presentation.dto.ExpressionUnitDto;
import local.myproject.scalc.services.ExpressionApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Выражения", description = "Операции вычисления и хранения выражений")
public class ExpressionApiController {
    private final ExpressionApiService expressionApiService;

    @GetMapping("/projects/{projectId}/expressions")
    @Operation(summary = "Получить выражения проекта", description = "Возвращает все выражения, принадлежащие проекту")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список выражений успешно получен"),
            @ApiResponse(responseCode = "404", description = "Проект не найден")
    })
    public List<ExpressionUnitDto> findAllByProject(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,
                                                    @PathVariable int projectId) {
        return expressionApiService.findAllByProject(principal.getUsername(), projectId);
    }

    @PostMapping("/projects/{projectId}/expressions/calculate")
    @Operation(summary = "Вычислить выражение", description = "Вычисляет выражение без сохранения в базе данных")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Выражение успешно вычислено"),
            @ApiResponse(responseCode = "404", description = "Проект не найден")
    })
    public ExpressionUnitDto calculate(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,
                                       @PathVariable int projectId,
                                       @RequestBody ExpressionUnitDto request) throws Exception {
        return expressionApiService.calculate(principal.getUsername(), projectId, request);
    }

    @PostMapping("/projects/{projectId}/expressions")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Сохранить выражение", description = "Создает новое выражение внутри проекта")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Выражение успешно создано"),
            @ApiResponse(responseCode = "404", description = "Проект не найден")
    })
    public ExpressionUnitDto create(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,
                                    @PathVariable int projectId,
                                    @RequestBody ExpressionUnitDto request) throws Exception {
        return expressionApiService.create(principal.getUsername(), projectId, request);
    }

    @GetMapping("/expressions/{expressionUnitId}")
    @Operation(summary = "Получить выражение", description = "Возвращает выражение по идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Выражение найдено"),
            @ApiResponse(responseCode = "404", description = "Выражение не найдено")
    })
    public ExpressionUnitDto findById(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,
                                      @PathVariable int expressionUnitId) {
        return expressionApiService.findById(principal.getUsername(), expressionUnitId);
    }

    @PutMapping("/expressions/{expressionUnitId}")
    @Operation(summary = "Обновить выражение", description = "Обновляет выражение и пересчитывает результат")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Выражение успешно обновлено"),
            @ApiResponse(responseCode = "404", description = "Выражение не найдено")
    })
    public ExpressionUnitDto update(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,
                                    @PathVariable int expressionUnitId,
                                    @RequestBody ExpressionUnitDto request) throws Exception {
        return expressionApiService.update(principal.getUsername(), expressionUnitId, request);
    }

    @DeleteMapping("/expressions/{expressionUnitId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить выражение", description = "Удаляет выражение по идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Выражение успешно удалено"),
            @ApiResponse(responseCode = "404", description = "Выражение не найдено")
    })
    public void delete(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,
                       @PathVariable int expressionUnitId) {
        expressionApiService.delete(principal.getUsername(), expressionUnitId);
    }
}

package local.myproject.scalc.presentation.controller.expression;

import local.myproject.scalc.application.expression.mapper.ExpressionApplicationMapper;
import local.myproject.scalc.application.expression.port.in.ExpressionUseCaseIn;
import local.myproject.scalc.presentation.dto.expression.ExpressionUnitDto;
import local.myproject.scalc.presentation.mapper.expression.ExpressionUnitPresentationMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-контроллер управления выражениями проекта.
 *
 * @author Evgenii Mironov
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Выражения", description = "Операции вычисления и хранения выражений")
public class ExpressionApiController {
    private final ExpressionUseCaseIn expressionUseCase;
    private final ExpressionApplicationMapper expressionApplicationMapper;
    private final ExpressionUnitPresentationMapper expressionUnitPresentationMapper;

    /**
     * Возвращает все выражения проекта.
     *
     * @param principal текущий пользователь
     * @param projectId идентификатор проекта
     * @return список DTO выражений
     */
    @GetMapping("/projects/{projectId}/expressions")
    @Operation(summary = "Получить выражения проекта", description = "Возвращает все выражения, принадлежащие проекту")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список выражений успешно получен"),
            @ApiResponse(responseCode = "404", description = "Проект не найден")
    })
    public List<ExpressionUnitDto> findAllByProject(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,
                                                    @PathVariable int projectId) {
        return expressionUseCase.findAllByProject(
                        expressionApplicationMapper.toFindAllByProjectQuery(principal.getUsername(), projectId)
                ).stream()
                .map(expressionUnitPresentationMapper::toDto)
                .toList();
    }

    /**
     * Вычисляет выражение без сохранения.
     *
     * @param principal текущий пользователь
     * @param projectId идентификатор проекта
     * @param request DTO выражения
     * @return DTO рассчитанного выражения
     * @throws Exception если выражение не удалось вычислить
     */
    @PostMapping("/projects/{projectId}/expressions/calculate")
    @Operation(summary = "Вычислить выражение", description = "Вычисляет выражение без сохранения в базе данных")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Выражение успешно вычислено"),
            @ApiResponse(responseCode = "404", description = "Проект не найден")
    })
    public ExpressionUnitDto calculate(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,
                                       @PathVariable int projectId,
                                       @RequestBody ExpressionUnitDto request) throws Exception {
        return expressionUnitPresentationMapper.toDto(
                expressionUseCase.calculate(
                        expressionApplicationMapper.toCalculateCommand(principal.getUsername(), projectId, request)
                )
        );
    }

    /**
     * Создает новое выражение в проекте.
     *
     * @param principal текущий пользователь
     * @param projectId идентификатор проекта
     * @param request DTO выражения
     * @return DTO созданного выражения
     * @throws Exception если выражение не удалось сохранить
     */
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
        return expressionUnitPresentationMapper.toDto(
                expressionUseCase.create(
                        expressionApplicationMapper.toCreateCommand(principal.getUsername(), projectId, request)
                )
        );
    }

    /**
     * Возвращает выражение по идентификатору.
     *
     * @param principal текущий пользователь
     * @param expressionUnitId идентификатор выражения
     * @return DTO найденного выражения
     */
    @GetMapping("/expressions/{expressionUnitId}")
    @Operation(summary = "Получить выражение", description = "Возвращает выражение по идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Выражение найдено"),
            @ApiResponse(responseCode = "404", description = "Выражение не найдено")
    })
    public ExpressionUnitDto findById(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,
                                      @PathVariable int expressionUnitId) {
        return expressionUnitPresentationMapper.toDto(
                expressionUseCase.findById(
                        expressionApplicationMapper.toFindByIdQuery(principal.getUsername(), expressionUnitId)
                )
        );
    }

    /**
     * Обновляет существующее выражение.
     *
     * @param principal текущий пользователь
     * @param expressionUnitId идентификатор выражения
     * @param request DTO выражения
     * @return DTO обновленного выражения
     * @throws Exception если выражение не удалось обновить
     */
    @PutMapping("/expressions/{expressionUnitId}")
    @Operation(summary = "Обновить выражение", description = "Обновляет выражение и пересчитывает результат")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Выражение успешно обновлено"),
            @ApiResponse(responseCode = "404", description = "Выражение не найдено")
    })
    public ExpressionUnitDto update(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,
                                    @PathVariable int expressionUnitId,
                                    @RequestBody ExpressionUnitDto request) throws Exception {
        return expressionUnitPresentationMapper.toDto(
                expressionUseCase.update(
                        expressionApplicationMapper.toUpdateCommand(principal.getUsername(), expressionUnitId, request)
                )
        );
    }

    /**
     * Удаляет выражение по идентификатору.
     *
     * @param principal текущий пользователь
     * @param expressionUnitId идентификатор выражения
     * @return результат не возвращается
     */
    @DeleteMapping("/expressions/{expressionUnitId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить выражение", description = "Удаляет выражение по идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Выражение успешно удалено"),
            @ApiResponse(responseCode = "404", description = "Выражение не найдено")
    })
    public void delete(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,
                       @PathVariable int expressionUnitId) {
        expressionUseCase.delete(
                expressionApplicationMapper.toDeleteCommand(principal.getUsername(), expressionUnitId)
        );
    }
}

package local.myproject.scalc.presentation.controller.project;

import local.myproject.scalc.application.project.mapper.ProjectApplicationMapper;
import local.myproject.scalc.application.project.port.in.ProjectUseCaseIn;
import local.myproject.scalc.presentation.dto.project.ProjectDto;
import local.myproject.scalc.presentation.mapper.project.ProjectPresentationMapper;
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
 * REST-контроллер управления проектами пользователя.
 *
 * @author Evgenii Mironov
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects")
@Tag(name = "Проекты", description = "Операции управления проектами пользователя")
public class ProjectApiController {
    private final ProjectUseCaseIn projectUseCase;
    private final ProjectApplicationMapper projectApplicationMapper;
    private final ProjectPresentationMapper projectPresentationMapper;

    /**
     * Возвращает список проектов текущего пользователя.
     *
     * @param principal текущий пользователь
     * @return список DTO проектов
     */
    @GetMapping
    @Operation(summary = "Получить список проектов", description = "Возвращает все проекты текущего пользователя")
    @ApiResponse(responseCode = "200", description = "Список проектов успешно получен")
    public List<ProjectDto> findAll(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
        return projectUseCase.findAll(projectApplicationMapper.toFindAllQuery(principal.getUsername())).stream()
                .map(projectPresentationMapper::toDto)
                .toList();
    }

    /**
     * Создает новый проект.
     *
     * @param principal текущий пользователь
     * @param request DTO проекта
     * @return DTO созданного проекта
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать проект", description = "Создает новый проект для текущего пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Проект успешно создан"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    })
    public ProjectDto create(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,
                             @RequestBody ProjectDto request) {
        return projectPresentationMapper.toDto(
                projectUseCase.create(projectApplicationMapper.toCreateCommand(principal.getUsername(), request))
        );
    }

    /**
     * Возвращает проект по идентификатору.
     *
     * @param principal текущий пользователь
     * @param projectId идентификатор проекта
     * @return DTO найденного проекта
     */
    @GetMapping("/{projectId}")
    @Operation(summary = "Получить проект", description = "Возвращает проект по идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Проект найден"),
            @ApiResponse(responseCode = "404", description = "Проект не найден")
    })
    public ProjectDto findById(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,
                               @PathVariable int projectId) {
        return projectPresentationMapper.toDto(
                projectUseCase.findById(projectApplicationMapper.toFindByIdQuery(principal.getUsername(), projectId))
        );
    }

    /**
     * Обновляет существующий проект.
     *
     * @param principal текущий пользователь
     * @param projectId идентификатор проекта
     * @param request DTO проекта
     * @return DTO обновленного проекта
     */
    @PutMapping("/{projectId}")
    @Operation(summary = "Обновить проект", description = "Обновляет название и описание проекта")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Проект успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Проект не найден")
    })
    public ProjectDto update(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,
                             @PathVariable int projectId,
                             @RequestBody ProjectDto request) {
        return projectPresentationMapper.toDto(
                projectUseCase.update(projectApplicationMapper.toUpdateCommand(principal.getUsername(), projectId, request))
        );
    }

    /**
     * Удаляет проект пользователя.
     *
     * @param principal текущий пользователь
     * @param projectId идентификатор проекта
     * @return результат не возвращается
     */
    @DeleteMapping("/{projectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить проект", description = "Удаляет проект текущего пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Проект успешно удален"),
            @ApiResponse(responseCode = "404", description = "Проект не найден")
    })
    public void delete(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,
                       @PathVariable int projectId) {
        projectUseCase.delete(projectApplicationMapper.toDeleteCommand(principal.getUsername(), projectId));
    }
}

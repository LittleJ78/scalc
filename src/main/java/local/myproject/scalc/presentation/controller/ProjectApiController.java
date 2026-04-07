package local.myproject.scalc.presentation.controller;

import local.myproject.scalc.presentation.dto.ProjectDto;
import local.myproject.scalc.services.ProjectApiService;
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
@RequestMapping("/api/v1/projects")
@Tag(name = "Проекты", description = "Операции управления проектами пользователя")
public class ProjectApiController {
    private final ProjectApiService projectApiService;

    @GetMapping
    @Operation(summary = "Получить список проектов", description = "Возвращает все проекты текущего пользователя")
    @ApiResponse(responseCode = "200", description = "Список проектов успешно получен")
    public List<ProjectDto> findAll(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
        return projectApiService.findAll(principal.getUsername());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать проект", description = "Создает новый проект для текущего пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Проект успешно создан"),
            @ApiResponse(responseCode = "401", description = "Пользователь не авторизован")
    })
    public ProjectDto create(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,
                             @RequestBody ProjectDto request) {
        return projectApiService.create(principal.getUsername(), request);
    }

    @GetMapping("/{projectId}")
    @Operation(summary = "Получить проект", description = "Возвращает проект по идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Проект найден"),
            @ApiResponse(responseCode = "404", description = "Проект не найден")
    })
    public ProjectDto findById(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,
                               @PathVariable int projectId) {
        return projectApiService.findById(principal.getUsername(), projectId);
    }

    @PutMapping("/{projectId}")
    @Operation(summary = "Обновить проект", description = "Обновляет название и описание проекта")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Проект успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Проект не найден")
    })
    public ProjectDto update(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,
                             @PathVariable int projectId,
                             @RequestBody ProjectDto request) {
        return projectApiService.update(principal.getUsername(), projectId, request);
    }

    @DeleteMapping("/{projectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить проект", description = "Удаляет проект текущего пользователя")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Проект успешно удален"),
            @ApiResponse(responseCode = "404", description = "Проект не найден")
    })
    public void delete(@AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,
                       @PathVariable int projectId) {
        projectApiService.delete(principal.getUsername(), projectId);
    }
}

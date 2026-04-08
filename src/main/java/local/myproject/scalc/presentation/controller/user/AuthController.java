package local.myproject.scalc.presentation.controller.user;

import local.myproject.scalc.application.user.mapper.AuthApplicationMapper;
import local.myproject.scalc.application.user.port.in.AuthUseCaseIn;
import local.myproject.scalc.presentation.dto.user.AuthResponseDto;
import local.myproject.scalc.presentation.dto.user.LoginRequestDto;
import local.myproject.scalc.presentation.dto.user.RegisterRequestDto;
import local.myproject.scalc.presentation.dto.user.UserDto;
import local.myproject.scalc.presentation.mapper.user.UserPresentationMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * REST-контроллер аутентификации и регистрации пользователей.
 *
 * @author Evgenii Mironov
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Аутентификация", description = "Операции регистрации и входа в систему")
public class AuthController {
    private final AuthUseCaseIn authUseCase;
    private final AuthApplicationMapper authApplicationMapper;
    private final UserPresentationMapper userPresentationMapper;

    /**
     * Регистрирует нового пользователя.
     *
     * @param request DTO запроса на регистрацию
     * @return DTO созданного пользователя
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Зарегистрировать пользователя", description = "Создает нового пользователя и возвращает его данные")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Пользователь успешно зарегистрирован"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации данных регистрации")
    })
    public UserDto register(@RequestBody RegisterRequestDto request) {
        return userPresentationMapper.toDto(authUseCase.register(authApplicationMapper.toRegisterCommand(request)));
    }

    /**
     * Выполняет вход пользователя.
     *
     * @param request DTO запроса на вход
     * @return DTO ответа аутентификации
     */
    @PostMapping("/login")
    @Operation(summary = "Выполнить вход", description = "Проверяет учетные данные и возвращает заглушку JWT-токена")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Вход выполнен успешно"),
            @ApiResponse(responseCode = "401", description = "Неверное имя пользователя или пароль")
    })
    public AuthResponseDto login(@RequestBody LoginRequestDto request) {
        return authUseCase.login(authApplicationMapper.toLoginCommand(request));
    }
}

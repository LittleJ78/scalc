package local.myproject.scalc.application.user.mapper;

import local.myproject.scalc.application.user.command.LoginCommand;
import local.myproject.scalc.application.user.command.RegisterUserCommand;
import local.myproject.scalc.presentation.dto.user.LoginRequestDto;
import local.myproject.scalc.presentation.dto.user.RegisterRequestDto;
import org.springframework.stereotype.Component;

/**
 * Маппер DTO аутентификации в команды application-слоя.
 *
 * @author Evgenii Mironov
 */
@Component
public class AuthApplicationMapper {
    /**
     * Преобразует DTO регистрации в команду регистрации.
     *
     * @param request DTO запроса на регистрацию
     * @return команда регистрации пользователя
     */
    public RegisterUserCommand toRegisterCommand(RegisterRequestDto request) {
        RegisterUserCommand command = new RegisterUserCommand();
        command.setUserName(request.userName());
        command.setPassword(request.password());
        command.setConfirmPassword(request.confirmPassword());
        command.setEmail(request.email());
        return command;
    }

    /**
     * Преобразует DTO входа в команду аутентификации.
     *
     * @param request DTO запроса на вход
     * @return команда входа пользователя
     */
    public LoginCommand toLoginCommand(LoginRequestDto request) {
        LoginCommand command = new LoginCommand();
        command.setUserName(request.userName());
        command.setPassword(request.password());
        return command;
    }
}

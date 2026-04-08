package local.myproject.scalc.application.user.port.in;

import local.myproject.scalc.application.user.command.LoginCommand;
import local.myproject.scalc.application.user.command.RegisterUserCommand;
import local.myproject.scalc.domain.aggregate.user.User;
import local.myproject.scalc.presentation.dto.user.AuthResponseDto;

/**
 * Входной порт сценариев аутентификации и регистрации.
 *
 * @author Evgenii Mironov
 */
public interface AuthUseCaseIn {
    /**
     * Регистрирует нового пользователя.
     *
     * @param command команда регистрации
     * @return созданный пользователь
     */
    User register(RegisterUserCommand command);

    /**
     * Выполняет аутентификацию пользователя.
     *
     * @param command команда входа
     * @return ответ аутентификации
     */
    AuthResponseDto login(LoginCommand command);
}

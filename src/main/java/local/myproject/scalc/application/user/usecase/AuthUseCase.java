package local.myproject.scalc.application.user.usecase;

import local.myproject.scalc.application.user.command.LoginCommand;
import local.myproject.scalc.application.user.command.RegisterUserCommand;
import local.myproject.scalc.application.user.port.in.AuthUseCaseIn;
import local.myproject.scalc.application.user.port.out.AuthenticationPort;
import local.myproject.scalc.application.user.port.out.UserPort;
import local.myproject.scalc.domain.aggregate.user.User;
import local.myproject.scalc.presentation.dto.user.AuthResponseDto;
import local.myproject.scalc.presentation.dto.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Use case аутентификации и регистрации пользователей.
 *
 * @author Evgenii Mironov
 */
@Service
@RequiredArgsConstructor
public class AuthUseCase implements AuthUseCaseIn {
    private static final long STUB_EXPIRES_IN_SECONDS = 3600;

    private final UserPort userPort;
    private final AuthenticationPort authenticationPort;

    /**
     * Регистрирует нового пользователя.
     *
     * @param command команда регистрации
     * @return созданный пользователь
     */
    public User register(RegisterUserCommand command) {
        User user = new User();
        user.setUserName(command.getUserName());
        user.setPassword(command.getPassword());
        user.setConfirmPassword(command.getConfirmPassword());
        user.setEmail(command.getEmail());

        String passwordValidation = user.validatePassword();
        if (!passwordValidation.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, passwordValidation);
        }

        userPort.save(user);
        return userPort.findByUserName(user.getUserName());
    }

    /**
     * Выполняет аутентификацию пользователя и формирует ответ.
     *
     * @param command команда входа
     * @return ответ аутентификации
     */
    public AuthResponseDto login(LoginCommand command) {
        authenticationPort.authenticate(command.getUserName(), command.getPassword());
        User user = userPort.findByUserName(command.getUserName());

        UserDto userDto = new UserDto(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getRoles().stream().map(Enum::name).collect(java.util.stream.Collectors.toSet())
        );

        return new AuthResponseDto(
                "stub-jwt-for-" + user.getUserName(),
                "Bearer",
                STUB_EXPIRES_IN_SECONDS,
                userDto
        );
    }
}

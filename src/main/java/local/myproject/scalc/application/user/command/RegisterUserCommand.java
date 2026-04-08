package local.myproject.scalc.application.user.command;

import lombok.Data;

/**
 * Команда регистрации нового пользователя.
 *
 * @author Evgenii Mironov
 */
@Data
public class RegisterUserCommand {
    private String userName;
    private String password;
    private String confirmPassword;
    private String email;
}

package local.myproject.scalc.application.user.command;

import lombok.Data;

/**
 * Команда входа пользователя в систему.
 *
 * @author Evgenii Mironov
 */
@Data
public class LoginCommand {
    private String userName;
    private String password;
}

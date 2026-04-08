package local.myproject.scalc.infrastructure.persistent.dto.user;

/**
 * DTO строки таблицы пользователей.
 *
 * @author Evgenii Mironov
 */
public record UserDbDto(
        Long userId,
        String userName,
        String password,
        String email
) {
}

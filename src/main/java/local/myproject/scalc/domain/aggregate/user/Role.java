package local.myproject.scalc.domain.aggregate.user;

import org.springframework.security.core.GrantedAuthority;

/**
 * Доступные роли пользователя в системе.
 *
 * @author Evgenii Mironov
 */
public enum Role implements GrantedAuthority {
    USER, ADMIN;

    @Override
    /**
     * Возвращает строковое представление роли для Spring Security.
     *
     * @param args параметры не передаются
     * @return имя роли
     */
    public String getAuthority() {
        return name();
    }
}

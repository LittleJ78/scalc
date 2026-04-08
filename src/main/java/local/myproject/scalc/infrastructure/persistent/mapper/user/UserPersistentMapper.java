package local.myproject.scalc.infrastructure.persistent.mapper.user;

import local.myproject.scalc.domain.aggregate.user.User;
import local.myproject.scalc.infrastructure.persistent.dto.user.UserDbDto;
import org.springframework.stereotype.Component;

/**
 * Маппер пользователей между доменной моделью и persistence DTO.
 *
 * @author Evgenii Mironov
 */
@Component
public class UserPersistentMapper {
    /**
     * Преобразует DTO базы данных в доменную модель пользователя.
     *
     * @param dto DTO строки базы данных
     * @return доменная модель пользователя
     */
    public User toEntity(UserDbDto dto) {
        User user = new User();
        user.setUserId(dto.userId());
        user.setUserName(dto.userName());
        user.setPassword(dto.password());
        user.setEmail(dto.email());
        return user;
    }

    /**
     * Преобразует доменную модель пользователя в DTO базы данных.
     *
     * @param user доменная модель пользователя
     * @return DTO базы данных
     */
    public UserDbDto toDto(User user) {
        return new UserDbDto(
                user.getUserId(),
                user.getUserName(),
                user.getPassword(),
                user.getEmail()
        );
    }
}

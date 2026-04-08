package local.myproject.scalc.presentation.mapper.user;

import local.myproject.scalc.domain.aggregate.user.User;
import local.myproject.scalc.presentation.dto.user.UserDto;
import org.springframework.stereotype.Component;

/**
 * Маппер пользователей между доменной моделью и REST DTO.
 *
 * @author Evgenii Mironov
 */
@Component
public class UserPresentationMapper {
    /**
     * Преобразует доменную модель пользователя в DTO.
     *
     * @param model доменная модель пользователя
     * @return DTO пользователя
     */
    public UserDto toDto(User model) {
        return new UserDto(
                model.getUserId(),
                model.getUserName(),
                model.getEmail(),
                model.getRoles().stream().map(Enum::name).collect(java.util.stream.Collectors.toSet())
        );
    }
}

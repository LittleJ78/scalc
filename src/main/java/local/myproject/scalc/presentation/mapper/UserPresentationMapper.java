package local.myproject.scalc.presentation.mapper;

import local.myproject.scalc.domain.User;
import local.myproject.scalc.presentation.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserPresentationMapper {
    public UserDto toDto(User model) {
        UserDto dto = new UserDto();
        dto.setUserId(model.getUserId());
        dto.setUserName(model.getUserName());
        dto.setEmail(model.getEmail());
        dto.setRoles(model.getRoles().stream().map(Enum::name).collect(java.util.stream.Collectors.toSet()));
        return dto;
    }
}

package local.myproject.scalc.persistent.mapper;

import local.myproject.scalc.domain.User;
import local.myproject.scalc.persistent.dto.UserDbDto;
import org.springframework.stereotype.Component;

@Component
public class UserPersistentMapper {
    public User toEntity(UserDbDto dto) {
        User user = new User();
        user.setUserId(dto.getUserId());
        user.setUserName(dto.getUserName());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        return user;
    }

    public UserDbDto toDto(User user) {
        UserDbDto dto = new UserDbDto();
        dto.setUserId(user.getUserId());
        dto.setUserName(user.getUserName());
        dto.setPassword(user.getPassword());
        dto.setEmail(user.getEmail());
        return dto;
    }
}

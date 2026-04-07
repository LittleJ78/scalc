package local.myproject.scalc.presentation.mapper;

import local.myproject.scalc.domain.AuthResponse;
import local.myproject.scalc.presentation.dto.AuthResponseDto;
import org.springframework.stereotype.Component;

@Component
public class AuthPresentationMapper {
    private final UserPresentationMapper userPresentationMapper;

    public AuthPresentationMapper(UserPresentationMapper userPresentationMapper) {
        this.userPresentationMapper = userPresentationMapper;
    }

    public AuthResponseDto toDto(AuthResponse response) {
        AuthResponseDto dto = new AuthResponseDto();
        dto.setToken(response.getToken());
        dto.setTokenType(response.getTokenType());
        dto.setExpiresIn(response.getExpiresIn());
        dto.setUser(userPresentationMapper.toDto(response.getUser()));
        return dto;
    }
}

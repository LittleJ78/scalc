package local.myproject.scalc.services;

import local.myproject.scalc.domain.AuthResponse;
import local.myproject.scalc.domain.User;
import local.myproject.scalc.presentation.dto.AuthResponseDto;
import local.myproject.scalc.presentation.dto.LoginRequestDto;
import local.myproject.scalc.presentation.dto.RegisterRequestDto;
import local.myproject.scalc.presentation.dto.UserDto;
import local.myproject.scalc.presentation.mapper.AuthPresentationMapper;
import local.myproject.scalc.presentation.mapper.UserPresentationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthApiService {
    private static final long STUB_EXPIRES_IN_SECONDS = 3600;

    private final UserServiceImpl userService;
    private final AuthenticationManager authenticationManager;
    private final UserPresentationMapper userPresentationMapper;
    private final AuthPresentationMapper authPresentationMapper;

    public UserDto register(RegisterRequestDto request) {
        User user = new User();
        user.setUserName(request.getUserName());
        user.setPassword(request.getPassword());
        user.setConfirmPassword(request.getConfirmPassword());
        user.setEmail(request.getEmail());

        String passwordValidation = user.validatePassword();
        if (!passwordValidation.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, passwordValidation);
        }

        userService.save(user);
        return userPresentationMapper.toDto(userService.findByUserName(user.getUserName()));
    }

    public AuthResponseDto login(LoginRequestDto request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword())
            );
        } catch (BadCredentialsException exception) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }

        User user = userService.findByUserName(request.getUserName());
        AuthResponse response = new AuthResponse();
        response.setToken("stub-jwt-for-" + user.getUserName());
        response.setTokenType("Bearer");
        response.setExpiresIn(STUB_EXPIRES_IN_SECONDS);
        response.setUser(user);
        return authPresentationMapper.toDto(response);
    }
}

package local.myproject.scalc.infrastructure.security;

import local.myproject.scalc.application.user.port.out.AuthenticationPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

/**
 * Адаптер порта аутентификации к Spring Security.
 *
 * @author Evgenii Mironov
 */
@Service
@RequiredArgsConstructor
public class SpringSecurityAuthenticationAdapter implements AuthenticationPort {
    private final AuthenticationManager authenticationManager;

    @Override
    /**
     * Выполняет аутентификацию пользователя через Spring Security.
     *
     * @param userName имя пользователя
     * @param password пароль пользователя
     * @return результат не возвращается
     */
    public void authenticate(String userName, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
        } catch (BadCredentialsException exception) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
    }
}

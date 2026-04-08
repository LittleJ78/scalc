package local.myproject.scalc.infrastructure.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Конфигурация безопасности приложения.
 *
 * @author Evgenii Mironov
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    /**
     * Создает кодировщик паролей BCrypt.
     *
     * @param args параметры не передаются
     * @return кодировщик паролей
     */
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    /**
     * Создает менеджер аутентификации Spring Security.
     *
     * @param authenticationConfiguration конфигурация аутентификации Spring
     * @return менеджер аутентификации
     * @throws Exception если не удалось получить менеджер аутентификации
     */
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    /**
     * Настраивает цепочку фильтров безопасности для HTTP-запросов.
     *
     * @param httpSecurity объект конфигурации HTTP-безопасности
     * @return настроенная цепочка фильтров
     * @throws Exception если во время настройки произошла ошибка
     */
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll()
                        .anyRequest().denyAll()
                )
                .httpBasic(basic -> {})
                .build();
    }
}

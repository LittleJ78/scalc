package local.myproject.scalc.presentation.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация OpenAPI и Swagger для REST API приложения.
 *
 * @author Evgenii Mironov
 */
@Configuration
public class OpenApiConfig {

    @Bean
    /**
     * Создает описание OpenAPI для публичного API приложения.
     *
     * @param args параметры не передаются
     * @return объект конфигурации OpenAPI
     */
    public OpenAPI scalcOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("SCalc API")
                        .description("REST API для работы с пользователями, проектами и математическими выражениями")
                        .version("v1")
                        .contact(new Contact().name("SCalc")))
                .addSecurityItem(new SecurityRequirement().addList("basicAuth"))
                .schemaRequirement("basicAuth", new SecurityScheme()
                        .name("basicAuth")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("basic")
                        .description("Временная схема авторизации до полноценного JWT"));
    }
}

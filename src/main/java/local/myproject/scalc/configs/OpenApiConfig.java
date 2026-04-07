package local.myproject.scalc.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
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

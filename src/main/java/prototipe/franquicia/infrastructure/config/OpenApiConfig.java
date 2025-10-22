package prototipe.franquicia.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestión de Franquicias")
                        .version("1.0.0")
                        .description("API RESTful reactiva para gestionar franquicias, sucursales y productos usando Spring Boot WebFlux")
                        .contact(new Contact()
                                .name("Realizado por Santiago Vela")
                                .email("santiago.vela.caicedo312@gmail.com")));
    }
}
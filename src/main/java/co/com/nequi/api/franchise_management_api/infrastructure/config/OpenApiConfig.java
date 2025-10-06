package co.com.nequi.api.franchise_management_api.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI franchiseAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Franchise Management API")
                        .description("API Reactiva para gestión de franquicias, sucursales y productos con arquitectura hexagonal")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Edward Tapiero")
                                .email("edward@example.com")
                                .url("https://github.com/edwardtapiero"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Development Server"),
                        new Server()
                                .url("https://api.production.com")
                                .description("Production Server")
                ));
    }

}

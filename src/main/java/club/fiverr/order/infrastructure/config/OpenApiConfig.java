package club.fiverr.order.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI orderServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FIVERREX. — Order & Escrow Service API")
                        .version("1.0.0")
                        .description("FIVERREX Order Lifecycle, Revisions & Escrow State Machine Microservice (DDD + Hexagonal Architecture)")
                        .contact(new Contact()
                                .name("FIVERREX Platform Engineering")
                                .url("https://fiverrex.club")))
                .servers(List.of(
                        new Server().url("http://localhost:8084").description("Order Service Direct (:8084)"),
                        new Server().url("http://localhost:8080").description("FIVERREX API Gateway (:8080)")
                ));
    }
}

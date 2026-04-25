package com.jhops10.music_request_api.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Music Request API")
                        .version("1.0.0")
                        .description("API para gerenciar solicitações de músicas entre alunos e professores.\n\n" +
                                "**Funcionalidades:**\n" +
                                "- Autenticação JWT com roles (STUDENT/TEACHER)\n" +
                                "- CRUD completo de pedidos de música\n" +
                                "- Regras de negócio para transições de status\n" +
                                "- Paginação\n" +
                                "- Mensageria assíncrona com RabbitMQ")
                        .contact(new Contact()
                                .name("João Paulo")
                                .email("joaowais@gmail.com")
                                .url("https://github.com/jhops10"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", createSecurityScheme()));
    }

    private SecurityScheme createSecurityScheme() {
        return new SecurityScheme()
                .name("Bearer Authentication")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Digite o token JWT no formato: Bearer {token}");
    }
}
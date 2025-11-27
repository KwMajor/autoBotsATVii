package com.autobots.automanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI automanagerApi() {
        return new OpenAPI()
            .info(new Info()
                .title("Automanager API")
                .description("API de cadastro de clientes, documentos, endereços e telefones.")
                .version("v1")
                .contact(new Contact().name("Equipe Autobots").email("contato@autobots.com"))
                .license(new License().name("MIT"))
            );
    }
}

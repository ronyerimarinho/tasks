package br.com.marinho.tasks.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Task Manager - DevInHouse")
                .version("v1")
                .description("Project for task management in DevInHouse")
                .termsOfService("")
                .license(new License().name("Apache 2.0").url("")));
    }
}

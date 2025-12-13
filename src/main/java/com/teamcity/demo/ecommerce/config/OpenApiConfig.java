package com.teamcity.demo.ecommerce.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name:E-Commerce Product Management}")
    private String applicationName;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("E-Commerce Product Management API")
                        .description("TeamCity Demo Project - A comprehensive product management system showcasing CI/CD best practices")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("TeamCity Demo Team")
                                .email("demo@teamcity.com")
                                .url("https://www.jetbrains.com/teamcity/"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
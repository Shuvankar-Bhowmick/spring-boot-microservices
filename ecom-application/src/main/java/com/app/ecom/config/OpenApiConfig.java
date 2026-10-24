package com.app.ecom.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "E-commerce API",
                version = "v1",
                description = "API documentation for the e-commerce application"
        )
)
public class OpenApiConfig {
}

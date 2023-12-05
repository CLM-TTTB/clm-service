package com.clm.api.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

/** OpenApiConfig */
@Configuration
@OpenAPIDefinition(
    info = @Info(title = "CLM API", version = "1.0", description = "Documentation CLM API v1.0"))
public class OpenApiConfig {}

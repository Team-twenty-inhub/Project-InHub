package com.twenty.inhub.base.appConfig;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition
public class SwaggerBoardConfig {

    @Bean
    public OpenAPI api() {
        Info info = new Info().title("IN-HUB OPEN API").version("v1").description("IN-HUB API 명세");

        return new OpenAPI().components(new Components()).info(info);
    }
}

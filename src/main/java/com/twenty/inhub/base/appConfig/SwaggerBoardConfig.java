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
        Info info = new Info().title("IN-HUB OPEN API").version("v1").description("개발자 등록이 완료되어야 사용할 수 있습니다.");

        return new OpenAPI().components(new Components()).info(info);
    }
}

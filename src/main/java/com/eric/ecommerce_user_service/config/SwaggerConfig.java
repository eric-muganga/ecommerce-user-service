package com.eric.ecommerce_user_service.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("E-commerce User Service API")
                        .version("1.0.0")
                        .description("API documentation for the E-commerce User Service")
                        .contact(new Contact()
                                .name("Eric")
                                .email("ericmuganga@outlook.com")
                                .url("https://eric-muganga-portfolio.vercel.app/")
                        )
                );
    }
}

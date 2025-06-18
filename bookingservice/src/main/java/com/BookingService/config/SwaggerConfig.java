package com.BookingService.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class SwaggerConfig {

    @Bean
    public OpenAPI bookingServiceOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("FlixShow Booking Service API")
                .version("1.0")
                .description("API documentation for Booking Service in FlixShow."))
            .addServersItem(new Server().url("http://localhost:8082").description("Local Server"));
    }
}

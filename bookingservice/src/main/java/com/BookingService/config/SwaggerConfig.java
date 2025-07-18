package com.BookingService.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.servers.Server;

import org.modelmapper.ModelMapper;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.BookingService.Interceptor.GatewayAuthFilter;

import org.springframework.context.annotation.Configuration;


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
            .addServersItem(new Server().url("http://localhost:9994").description("Local Server"));
    }
    
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
    
    @Bean
    public FilterRegistrationBean<GatewayAuthFilter> registerFilter() {
        FilterRegistrationBean<GatewayAuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new GatewayAuthFilter());
        registrationBean.addUrlPatterns("/v1/*"); // Secure all APIs
        return registrationBean;
    }
}

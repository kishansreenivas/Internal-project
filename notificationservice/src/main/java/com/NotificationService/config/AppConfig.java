package com.NotificationService.config;

import org.modelmapper.ModelMapper;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;


@Configuration
public class AppConfig {

 @Bean
 public OpenAPI customOpenAPI() {
     return new OpenAPI().info(new Info()
         .title("FlixShow Notification Service API")
         .version("1.0")
         .description("API for managing user accounts in FlixShow, including admin roles"));
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

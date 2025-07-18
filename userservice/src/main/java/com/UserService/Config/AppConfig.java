package com.UserService.Config;

import org.modelmapper.ModelMapper;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.UserService.Interceptor.GatewayAuthFilter;


@Configuration
public class AppConfig {
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

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
    }

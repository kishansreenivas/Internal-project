package com.PaymentService.Config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class WebClientConfig {
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
    @Bean
    public FilterRegistrationBean<GatewayAuthFilter> registerFilter() {
        FilterRegistrationBean<GatewayAuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new GatewayAuthFilter());
        registrationBean.addUrlPatterns("/v1/*"); // Secure all APIs
        return registrationBean;
    }
  
}
package com.UserService.Interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;

@Configuration
public class BookingClientConfig {

	@Value("${gateway.secret:testing}")
	private String gatewaySecret;
	
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
        	 requestTemplate.header("Gateway-Secret", gatewaySecret);
            requestTemplate.header("Accept", "application/json");
            requestTemplate.header("Content-Type", "application/json");
        };
    }



}

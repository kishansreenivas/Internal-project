package com.UserService.Config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EnvPropertyLoader {

    @Value("${spring.application.name:default-app}")
    private String appName;

    @Value("${server.port:9991}")
    private int serverPort;

    @PostConstruct
    public void preloadProperties() {
        log.info("Application Name: {}", appName);
        log.info("Running on Port: {}", serverPort);
    }
}

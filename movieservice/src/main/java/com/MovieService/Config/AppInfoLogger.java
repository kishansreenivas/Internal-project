package com.MovieService.Config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class AppInfoLogger {

    @Value("${app.environment}")
    private String environment;

    @Value("${app.version}")
    private String version;

    @Value("${app.owner}")
    private String owner;

    @PostConstruct
    public void logInfo() {
        System.out.println("App Environment: " + environment);
        System.out.println("App Version: " + version);
        System.out.println("App Owner: " + owner);
    }
}

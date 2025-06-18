package com.MovieService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

//@ComponentScan(basePackages = {"com.Movie_Service","Controller","Services"})
//@EnableJpaRepositories(basePackages = "Repository")
//@EntityScan(basePackages = "Entity")
public class MovieServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieServiceApplication.class, args);
	}

}

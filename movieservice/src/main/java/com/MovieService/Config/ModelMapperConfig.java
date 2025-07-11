package com.MovieService.Config;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.MovieService.dto.ShowtimeRequestDto;
import com.MovieService.Entity.Showtime;

@Configuration
public class ModelMapperConfig {  // Renamed to follow class naming conventions

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.addMappings(new PropertyMap<ShowtimeRequestDto, Showtime>() {
            @Override
            protected void configure() {
                skip(destination.getId());
                skip(destination.getMovie());
                skip(destination.getScreen());
                skip(destination.getTheatre());
            }
        });

        return mapper;
    }
}

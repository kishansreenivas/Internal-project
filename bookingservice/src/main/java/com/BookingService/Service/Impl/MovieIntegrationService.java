package com.BookingService.Service.Impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.BookingService.Dto.MovieDto;
import com.BookingService.External.Service.MovieClient;

@Service
public class MovieIntegrationService {

    @Autowired
    private MovieClient movieClient;

    public MovieDto createMovie(MovieDto movie) {
        return movieClient.createMovie(movie).getData();
    }

    public List<MovieDto> getAllMovies() {
        return movieClient.getAllMovies().getData();
    }

    public MovieDto getMovieById(String id) {
        return movieClient.getMovieById(id).getData();
    }

    public MovieDto updateMovie(String id, MovieDto movie) {
        return movieClient.updateMovie(id, movie).getData();
    }

    public Map<String, String> deleteMovie(String id) {
        return movieClient.deleteMovie(id).getData();
    }
}

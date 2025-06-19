package com.MovieService.Services;


import com.MovieService.Entity.Movie;

import java.util.List;

public interface MovieService {
    Movie createMovie(Movie movie);
    List<Movie> getAllMovies();
    Movie getMovieById(String id);
    Movie updateMovie(String id, Movie updatedMovie);
    void deleteMovie(String id);
}

package com.MovieService.Services;

import java.util.List;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.MovieService.Entity.Movie;
import com.MovieService.Exception.ResourceNotFoundException;
import com.MovieService.Repository.MovieRepository;


@Service
public class MovieService {
	@Autowired
    private final MovieRepository movieRepository;
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie createMovie(Movie movie) {
        try {
            return movieRepository.save(movie);
        } catch (Exception e) {
            throw new ServiceException("Error creating movie", e);
        }
    }

    public List<Movie> getAllMovies() {
        try {
            return movieRepository.findAll();
        } catch (Exception e) {
            throw new ServiceException("Error retrieving movies", e);
        }
    }

    public Movie getMovieById(String id) {
        return movieRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Movie not found with ID: " + id));
    }

    public Movie updateMovie(String id, Movie updatedMovie) {
        Movie movie = movieRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
        movie.setTitle(updatedMovie.getTitle());
        movie.setLanguage(updatedMovie.getLanguage());
        movie.setReleaseDate(updatedMovie.getReleaseDate());
        movie.setDurationMinutes(updatedMovie.getDurationMinutes());
        movie.setStatus(updatedMovie.getStatus());
        movie.setGenres(updatedMovie.getGenres());
        return movieRepository.save(movie);
    }

    public void deleteMovie(String id) {
        if (!movieRepository.existsById(id)) {
            throw new ResourceNotFoundException("Movie not found with ID: " + id);
        }
        movieRepository.deleteById(id);
    }
}
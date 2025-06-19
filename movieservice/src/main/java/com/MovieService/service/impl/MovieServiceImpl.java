package com.MovieService.service.impl;

import com.MovieService.Entity.Movie;
import com.MovieService.Exception.ResourceNotFoundException;
import com.MovieService.Repository.MovieRepository;
import com.MovieService.Services.MovieService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Override
    public Movie createMovie(Movie movie) {
        log.info("ENTRY: createMovie() with movie: {}", movie);
        try {
            Movie saved = movieRepository.save(movie);
            log.info("EXIT: createMovie() saved movie: {}", saved);
            return saved;
        } catch (Exception e) {
            log.error("ERROR: createMovie() failed", e);
            throw new ServiceException("Error creating movie", e);
        }
    }

    @Override
    public List<Movie> getAllMovies() {
        log.info("ENTRY: getAllMovies()");
        try {
            List<Movie> movies = movieRepository.findAll();
            log.info("EXIT: getAllMovies() found {} movies", movies.size());
            return movies;
        } catch (Exception e) {
            log.error("ERROR: getAllMovies() failed", e);
            throw new ServiceException("Error retrieving movies", e);
        }
    }

    @Override
    public Movie getMovieById(String id) {
        log.info("ENTRY: getMovieById() with id: {}", id);
        return movieRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("VALIDATION FAILED: Movie not found with ID: {}", id);
                    return new ResourceNotFoundException("Movie not found with ID: " + id);
                });
    }

    @Override
    public Movie updateMovie(String id, Movie updatedMovie) {
        log.info("ENTRY: updateMovie() with id: {}, updated data: {}", id, updatedMovie);
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("VALIDATION FAILED: Movie not found with ID: {}", id);
                    return new EntityNotFoundException("Movie not found");
                });

        movie.setTitle(updatedMovie.getTitle());
        movie.setLanguage(updatedMovie.getLanguage());
        movie.setReleaseDate(updatedMovie.getReleaseDate());
        movie.setDurationMinutes(updatedMovie.getDurationMinutes());
        movie.setStatus(updatedMovie.getStatus());
        movie.setGenres(updatedMovie.getGenres());

        Movie saved = movieRepository.save(movie);
        log.info("EXIT: updateMovie() updated movie: {}", saved);
        return saved;
    }

    @Override
    public void deleteMovie(String id) {
        log.info("ENTRY: deleteMovie() with id: {}", id);
        if (!movieRepository.existsById(id)) {
            log.warn("VALIDATION FAILED: Movie not found with ID: {}", id);
            throw new ResourceNotFoundException("Movie not found with ID: " + id);
        }
        movieRepository.deleteById(id);
        log.info("EXIT: deleteMovie() successful for ID: {}", id);
    }
}

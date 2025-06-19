package com.MovieService.service.impl;

import com.MovieService.Entity.Genre;
import com.MovieService.Exception.ResourceNotFoundException;
import com.MovieService.Repository.GenreRepository;
import com.MovieService.Services.GenreService;
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
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final MovieService movieService; // If needed for future features

    @Override
    public Genre createGenre(Genre genre) {
        log.info("ENTRY: createGenre() with data: {}", genre);
        try {
            Genre saved = genreRepository.save(genre);
            log.info("EXIT: createGenre() saved genre: {}", saved);
            return saved;
        } catch (Exception e) {
            log.error("ERROR: createGenre() failed", e);
            throw new ServiceException("Error creating genre", e);
        }
    }

    @Override
    public List<Genre> getAllGenres() {
        log.info("ENTRY: getAllGenres()");
        try {
            List<Genre> genres = genreRepository.findAll();
            log.info("EXIT: getAllGenres() - {} genres found", genres.size());
            return genres;
        } catch (Exception e) {
            log.error("ERROR: getAllGenres() failed", e);
            throw new ServiceException("Error retrieving genres", e);
        }
    }

    @Override
    public Genre getGenreById(String id) {
        log.info("ENTRY: getGenreById() with ID: {}", id);
        return genreRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("VALIDATION FAILED: Genre not found with ID: {}", id);
                    return new ResourceNotFoundException("Genre not found with ID: " + id);
                });
    }

    @Override
    public void deleteGenre(String id) {
        log.info("ENTRY: deleteGenre() with ID: {}", id);
        if (!genreRepository.existsById(id)) {
            log.warn("VALIDATION FAILED: Genre not found with ID: {}", id);
            throw new ResourceNotFoundException("Genre not found with ID: " + id);
        }
        genreRepository.deleteById(id);
        log.info("EXIT: deleteGenre() successful for ID: {}", id);
    }
}

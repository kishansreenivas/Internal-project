package com.Movie_Service;


import com.MovieService.Entity.Movie;
import com.MovieService.EnumType.MovieStatus;
import com.MovieService.Exception.ResourceNotFoundException;
import com.MovieService.Repository.MovieRepository;
import com.MovieService.service.impl.MovieServiceImpl;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
class MovieServiceImplTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieServiceImpl movieService;

    private Movie getSampleMovie() {
        Movie movie = new Movie();
        movie.setId(UUID.randomUUID().toString());
        movie.setTitle("Inception");
        movie.setLanguage("English");
        movie.setReleaseDate(LocalDateTime.of(2027, 8, 17, 0, 0));
        movie.setDurationMinutes(148);
        movie.setStatus(MovieStatus.UPCOMING);
        return movie;
    }
    
    @Test
    void createMovie_shouldSaveAndReturnMovie() {
        Movie movie = getSampleMovie();
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        Movie result = movieService.createMovie(movie);

        assertNotNull(result);
        assertEquals("Inception", result.getTitle());
        verify(movieRepository, times(1)).save(movie);
    }
    
    @Test
    void getAllMovies_shouldReturnList() {
        when(movieRepository.findAll()).thenReturn(Arrays.asList(getSampleMovie(), getSampleMovie()));

        var result = movieService.getAllMovies();

        assertEquals(2, result.size());
        verify(movieRepository, times(1)).findAll();
    }
    
    @Test
    void getMovieById_shouldReturnMovie_whenExists() {
        Movie movie = getSampleMovie();
        when(movieRepository.findById(movie.getId())).thenReturn(Optional.of(movie));

        Movie result = movieService.getMovieById(movie.getId());

        assertNotNull(result);
        assertEquals(movie.getId(), result.getId());
    }

    @Test
    void getMovieById_shouldThrow_whenNotFound() {
        when(movieRepository.findById("123")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> movieService.getMovieById("123"));
    }

    @Test
    void updateMovie_shouldUpdateAndReturnMovie() {
        Movie original = getSampleMovie();
        Movie updated = getSampleMovie();
        updated.setTitle("Updated Title");

        when(movieRepository.findById(original.getId())).thenReturn(Optional.of(original));
        when(movieRepository.save(any(Movie.class))).thenReturn(updated);

        Movie result = movieService.updateMovie(original.getId(), updated);

        assertEquals("Updated Title", result.getTitle());
    }

    @Test
    void deleteMovie_shouldDelete_whenExists() {
        String movieId = "abc-123";
        when(movieRepository.existsById(movieId)).thenReturn(true);

        movieService.deleteMovie(movieId);

        verify(movieRepository, times(1)).deleteById(movieId);
    }

    @Test
    void deleteMovie_shouldThrow_whenNotFound() {
        String movieId = "not-found";
        when(movieRepository.existsById(movieId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> movieService.deleteMovie(movieId));
    }
}

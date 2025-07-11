package com.Movie_Service;

import com.MovieService.Entity.Movie;
import com.MovieService.Entity.Screen;
import com.MovieService.Entity.Showtime;
import com.MovieService.Entity.Theatre;
import com.MovieService.Exception.ResourceNotFoundException;
import com.MovieService.Repository.*;
import com.MovieService.dto.ShowtimeRequestDto;
import com.MovieService.service.impl.ShowtimeServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShowtimeServiceImplTest {

    @Mock
    private ShowtimeRepository showtimeRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private TheatreRepository theatreRepository;

    @Mock
    private ScreenRepository screenRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    @InjectMocks
    private ShowtimeServiceImpl showtimeService;

    private Movie movie;
    private Theatre theatre;
    private Screen screen;
    private ShowtimeRequestDto requestDto;

    @BeforeEach
    void setup() {
        movie = new Movie();
        movie.setId("m1");

        theatre = new Theatre();
        theatre.setId("t1");

        screen = new Screen();
        screen.setId("s1");

        requestDto = new ShowtimeRequestDto();
        requestDto.setMovieId("m1");
        requestDto.setTheatreId("t1");
        requestDto.setScreenId("s1");
    }


    @Test
    void createShowtime_shouldThrowWhenMovieMissing() {
        when(movieRepository.findById("m1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> showtimeService.createShowtime(requestDto));
    }

    @Test
    void createShowtime_shouldThrowWhenTheatreMissing() {
        when(movieRepository.findById("m1")).thenReturn(Optional.of(movie));
        when(theatreRepository.findById("t1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> showtimeService.createShowtime(requestDto));
    }

    @Test
    void createShowtime_shouldThrowWhenScreenMissing() {
        when(movieRepository.findById("m1")).thenReturn(Optional.of(movie));
        when(theatreRepository.findById("t1")).thenReturn(Optional.of(theatre));
        when(screenRepository.findById("s1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> showtimeService.createShowtime(requestDto));
    }

    @Test
    void getAllShowtimes_shouldReturnList() {
        List<Showtime> list = Arrays.asList(new Showtime(), new Showtime());
        when(showtimeRepository.findAll()).thenReturn(list);

        List<Showtime> result = showtimeService.getAllShowtimes();

        assertEquals(2, result.size());
        verify(showtimeRepository).findAll();
    }

    @Test
    void getShowtimeById_shouldReturnShowtime() {
        Showtime showtime = new Showtime();
        showtime.setId("sh1");

        when(showtimeRepository.findById("sh1")).thenReturn(Optional.of(showtime));

        Showtime result = showtimeService.getShowtimeById("sh1");

        assertEquals("sh1", result.getId());
    }

    @Test
    void getShowtimeById_shouldThrowWhenNotFound() {
        when(showtimeRepository.findById("sh1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> showtimeService.getShowtimeById("sh1"));
    }

  
    @Test
    void updateShowtime_shouldThrowWhenNotFound() {
        Showtime updated = new Showtime();

        when(showtimeRepository.findById("sh1")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> showtimeService.updateShowtime("sh1", updated));
    }

    @Test
    void deleteShowtime_shouldDeleteIfExists() {
        when(showtimeRepository.existsById("sh1")).thenReturn(true);
        doNothing().when(showtimeRepository).deleteById("sh1");

        showtimeService.deleteShowtime("sh1");

        verify(showtimeRepository).deleteById("sh1");
    }

    @Test
    void deleteShowtime_shouldThrowIfNotExists() {
        when(showtimeRepository.existsById("sh1")).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> showtimeService.deleteShowtime("sh1"));
    }
}

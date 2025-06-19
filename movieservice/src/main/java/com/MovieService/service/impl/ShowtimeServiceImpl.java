package com.MovieService.service.impl;

import com.MovieService.Entity.Movie;
import com.MovieService.Entity.Screen;
import com.MovieService.Entity.Showtime;
import com.MovieService.Entity.Theatre;
import com.MovieService.Exception.ResourceNotFoundException;
import com.MovieService.Repository.MovieRepository;
import com.MovieService.Repository.ScreenRepository;
import com.MovieService.Repository.ShowtimeRepository;
import com.MovieService.Repository.TheatreRepository;
import com.MovieService.Services.ShowtimeService;
import com.MovieService.dto.ShowtimeRequestDto;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShowtimeServiceImpl implements ShowtimeService {

    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;
    private final TheatreRepository theatreRepository;
    private final ScreenRepository screenRepository;

    @Override
    public Showtime createShowtime(ShowtimeRequestDto dto) {
        log.info("ENTRY: createShowtime() with data: {}", dto);

        if (dto.getMovieId() == null || dto.getTheatreId() == null || dto.getScreenId() == null) {
            log.warn("VALIDATION FAILED: Missing required IDs");
            throw new IllegalArgumentException("Movie ID, Theatre ID, and Screen ID must not be null");
        }

        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> {
                    log.warn("VALIDATION FAILED: Movie not found with ID: {}", dto.getMovieId());
                    return new ResourceNotFoundException("Movie not found");
                });

        Theatre theatre = theatreRepository.findById(dto.getTheatreId())
                .orElseThrow(() -> {
                    log.warn("VALIDATION FAILED: Theatre not found with ID: {}", dto.getTheatreId());
                    return new ResourceNotFoundException("Theatre not found");
                });

        Screen screen = screenRepository.findById(dto.getScreenId())
                .orElseThrow(() -> {
                    log.warn("VALIDATION FAILED: Screen not found with ID: {}", dto.getScreenId());
                    return new ResourceNotFoundException("Screen not found");
                });

        Showtime showtime = new Showtime();
        showtime.setMovie(movie);
        showtime.setTheatre(theatre);
        showtime.setScreen(screen);
        showtime.setShowStart(dto.getShowStart());
        showtime.setShowEnd(dto.getShowEnd());
        showtime.setLanguage(dto.getLanguage());

        Showtime saved = showtimeRepository.save(showtime);
        log.info("EXIT: createShowtime() created showtime: {}", saved);
        return saved;
    }

    @Override
    public List<Showtime> getAllShowtimes() {
        log.info("ENTRY: getAllShowtimes()");
        try {
            List<Showtime> list = showtimeRepository.findAll();
            log.info("EXIT: getAllShowtimes() found {} showtimes", list.size());
            return list;
        } catch (Exception e) {
            log.error("ERROR: Failed to retrieve showtimes", e);
            throw new ServiceException("Error retrieving getAllShowtimes", e);
        }
    }

    @Override
    public Showtime getShowtimeById(String id) {
        log.info("ENTRY: getShowtimeById() with id: {}", id);
        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("VALIDATION FAILED: Showtime not found with ID {}", id);
                    return new ResourceNotFoundException("Showtime not found with ID: " + id);
                });
        log.info("EXIT: getShowtimeById() returning: {}", showtime);
        return showtime;
    }

    @Override
    public Showtime updateShowtime(String id, Showtime updated) {
        log.info("ENTRY: updateShowtime() with id: {}, updated data: {}", id, updated);

        Showtime showtime = showtimeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("VALIDATION FAILED: Showtime not found with ID {}", id);
                    return new EntityNotFoundException("Showtime not found");
                });

        showtime.setMovie(updated.getMovie());
        showtime.setTheatre(updated.getTheatre());
        showtime.setScreen(updated.getScreen());
        showtime.setShowStart(updated.getShowStart());
        showtime.setShowEnd(updated.getShowEnd());
        showtime.setLanguage(updated.getLanguage());

        Showtime saved = showtimeRepository.save(showtime);
        log.info("EXIT: updateShowtime() updated showtime: {}", saved);
        return saved;
    }

    @Override
    public void deleteShowtime(String id) {
        log.info("ENTRY: deleteShowtime() with id: {}", id);
        if (!showtimeRepository.existsById(id)) {
            log.warn("VALIDATION FAILED: Showtime with ID {} does not exist", id);
            throw new ResourceNotFoundException("Showtime not found with ID: " + id);
        }
        showtimeRepository.deleteById(id);
        log.info("EXIT: deleteShowtime() success for id: {}", id);
    }
}

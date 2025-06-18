package com.MovieService.Services;

import java.util.List;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.MovieService.Entity.Movie;
import com.MovieService.Entity.Screen;
import com.MovieService.Entity.Showtime;
import com.MovieService.Entity.Theatre;
import com.MovieService.Exception.ResourceNotFoundException;
import com.MovieService.Repository.MovieRepository;
import com.MovieService.Repository.ScreenRepository;
import com.MovieService.Repository.ShowtimeRepository;
import com.MovieService.Repository.TheatreRepository;
import com.MovieService.dto.ShowtimeRequestDto;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ShowtimeService {
	@Autowired
    private final ShowtimeRepository showtimeRepository;
	@Autowired
    private final MovieRepository movieRepository;
	
	@Autowired
    private final TheatreRepository theatreRepository;
	
	@Autowired
    private final ScreenRepository screenRepository;
	

	public Showtime createShowtime(ShowtimeRequestDto dto) {
	    if (dto.getMovieId() == null || dto.getTheatreId() == null || dto.getScreenId() == null) {
	        throw new IllegalArgumentException("Movie ID, Theatre ID, and Screen ID must not be null");
	    }

	    Movie movie = movieRepository.findById(dto.getMovieId())
	        .orElseThrow(() -> new RuntimeException("Movie not found"));

	    Theatre theatre = theatreRepository.findById(dto.getTheatreId())
	        .orElseThrow(() -> new RuntimeException("Theatre not found"));

	    Screen screen = screenRepository.findById(dto.getScreenId())
	        .orElseThrow(() -> new RuntimeException("Screen not found"));

	    Showtime showtime = new Showtime();
	    showtime.setMovie(movie);
	    showtime.setTheatre(theatre);
	    showtime.setScreen(screen);
	    showtime.setShowStart(dto.getShowStart());
	    showtime.setShowEnd(dto.getShowEnd());
	    showtime.setLanguage(dto.getLanguage());

	    return showtimeRepository.save(showtime);
	}


    public List<Showtime> getAllShowtimes() {
    	try {
    		return showtimeRepository.findAll();
		} catch (Exception e) {
			 throw new ServiceException("Error retrieving getAllShowtimes", e);
		}

    }

    public Showtime getShowtimeById(String id) {
        return showtimeRepository.findById(id)
        		 .orElseThrow(() -> new ResourceNotFoundException("Movie not found with ID: " + id));
    }

    public Showtime updateShowtime(String id, Showtime updated) {
        Showtime showtime = showtimeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Showtime not found"));
        showtime.setMovie(updated.getMovie());
        showtime.setTheatre(updated.getTheatre());
        showtime.setScreen(updated.getScreen());
        showtime.setShowStart(updated.getShowStart());
        showtime.setShowEnd(updated.getShowEnd());
        showtime.setLanguage(updated.getLanguage());
        return showtimeRepository.save(showtime);
    }

    public void deleteShowtime(String id) {
    	 if (!showtimeRepository.existsById(id)) {
    	        throw new ResourceNotFoundException("Movie not found with ID: " + id);
    	    }
    	 showtimeRepository.deleteById(id);
    	    }
}
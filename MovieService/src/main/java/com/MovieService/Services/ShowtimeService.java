package com.MovieService.Services;

import java.util.List;
import java.util.Optional;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.MovieService.Entity.Showtime;
import com.MovieService.Repository.ShowtimeRepository;

import Exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;


@Service
public class ShowtimeService {
	@Autowired
    private final ShowtimeRepository showtimeRepository;
    public ShowtimeService(ShowtimeRepository showtimeRepository) {
        this.showtimeRepository = showtimeRepository;
    }

    public Showtime createShowtime(Showtime showtime) {
    	try {
    		 return showtimeRepository.save(showtime);
		} catch (Exception e) {
			throw new ServiceException("Error creating showtime", e);
		} 
       
    }

    public List<Showtime> getAllShowtimes() {
    	try {
    		return showtimeRepository.findAll();
		} catch (Exception e) {
			 throw new ServiceException("Error retrieving getAllShowtimes", e);
		}
        
    }

    public Showtime getShowtimeById(Long id) {
        return showtimeRepository.findById(id)
        		 .orElseThrow(() -> new ResourceNotFoundException("Movie not found with ID: " + id));
    }

    public Showtime updateShowtime(Long id, Showtime updated) {
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

    public void deleteShowtime(Long id) {
    	 if (!showtimeRepository.existsById(id)) {
    	        throw new ResourceNotFoundException("Movie not found with ID: " + id);
    	    }
    	 showtimeRepository.deleteById(id);
    	    }
}
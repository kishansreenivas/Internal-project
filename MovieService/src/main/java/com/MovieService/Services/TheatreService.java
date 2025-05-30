package com.MovieService.Services;

import java.util.List;
import java.util.Optional;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.MovieService.Entity.Theatre;
import com.MovieService.Repository.TheatreRepository;

import Exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class TheatreService {
	@Autowired
    private final TheatreRepository theatreRepository;
    public TheatreService(TheatreRepository theatreRepository) {
        this.theatreRepository = theatreRepository;
    }

    public Theatre createTheatre(Theatre theatre) {
    	try {
    		 return theatreRepository.save(theatre);
		} catch (Exception e) {
			throw new ServiceException("Error creating Theatre", e);
		}
       
    }

    public List<Theatre> getAllTheatres() {
    	try {
    		 return theatreRepository.findAll();
		} catch (Exception e) {
			  throw new ServiceException("Error retrieving getAllTheatres", e);
		}
       
    }

    public Theatre getTheatreById(Long id) {
        return theatreRepository.findById(id)
        		 .orElseThrow(() -> new ResourceNotFoundException("Movie not found with ID: " + id));
    }

    public Theatre updateTheatre(Long id, Theatre updated) {
        Theatre theatre = theatreRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Theatre not found"));
        theatre.setName(updated.getName());
        theatre.setAddress(updated.getAddress());
        theatre.setCity(updated.getCity());
        theatre.setState(updated.getState());
        theatre.setPincode(updated.getPincode());
        theatre.setLatitude(updated.getLatitude());
        theatre.setLongitude(updated.getLongitude());
        return theatreRepository.save(theatre);
    }

    public void deleteTheatre(Long id) {
 
    if (!theatreRepository.existsById(id)) {
        throw new ResourceNotFoundException("Movie not found with ID: " + id);
    }
    theatreRepository.deleteById(id);
    }
    
    
}

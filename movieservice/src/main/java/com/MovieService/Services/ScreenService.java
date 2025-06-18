package com.MovieService.Services;

import java.util.List;
import java.util.Optional;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.MovieService.Entity.Screen;
import com.MovieService.Entity.Theatre;
import com.MovieService.Repository.ScreenRepository;
import com.MovieService.Repository.TheatreRepository;
import com.MovieService.dto.ScreenRequestDto;

import jakarta.persistence.EntityNotFoundException;


@Service
public class ScreenService {
	@Autowired
    private final ScreenRepository screenRepository;
	
	  @Autowired
	    private TheatreRepository theatreRepository;

	
    public ScreenService(ScreenRepository screenRepository) {
        this.screenRepository = screenRepository;
    }

    public Screen createScreen(Screen screen) {
    	try {
    		 return screenRepository.save(screen);
		} catch (Exception e) {
			 throw new ServiceException("Error retriving screens",e);
		}

    }

    public List<Screen> getAllScreens() {
        try {
        	return screenRepository.findAll();
		} catch (Exception e) {
			throw new ServiceException("Error retriving getAllScreens",e);
		}
    }

    public Optional<Screen> getScreenById(String id) {
       try {
    	   return screenRepository.findById(id);
	} catch (Exception e) {
		throw new ServiceException("Error retriving getScreenById",e);
	}
    }

    public Screen updateScreen(String id, Screen updated) {
        Screen screen = screenRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Screen not found"));
        screen.setName(updated.getName());
        screen.setTotalSeats(updated.getTotalSeats());
        screen.setTheatre(updated.getTheatre());
        return screenRepository.save(screen);
    }

    public void deleteScreen(String id) {
       try {
    	   screenRepository.deleteById(id);
	} catch (Exception e) {
		throw new ServiceException("Error retriving delete Screen",e);
	}
    }
    
  
    public Screen createScreen(ScreenRequestDto dto) {
        Theatre theatre = theatreRepository.findById(dto.getTheatreId())
            .orElseThrow(() -> new RuntimeException("Theatre not found"));

        Screen screen = new Screen();
        screen.setName(dto.getName());
        screen.setTheatre(theatre); // ✅ This is crucial
        // set other fields

        return screenRepository.save(screen);
    }

}

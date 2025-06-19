package com.MovieService.service.impl;

import com.MovieService.Entity.Theatre;
import com.MovieService.Exception.ResourceNotFoundException;
import com.MovieService.Repository.TheatreRepository;
import com.MovieService.Services.TheatreService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TheatreServiceImpl implements TheatreService {

    private final TheatreRepository theatreRepository;

    @Override
    public Theatre createTheatre(Theatre theatre) {
        log.info("ENTRY: createTheatre() with data: {}", theatre);
        try {
            Theatre saved = theatreRepository.save(theatre);
            log.info("EXIT: createTheatre() with result: {}", saved);
            return saved;
        } catch (Exception e) {
            log.error("Error creating theatre: {}", e.getMessage(), e);
            throw new ServiceException("Error creating Theatre", e);
        }
    }

    @Override
    public List<Theatre> getAllTheatres() {
        log.info("ENTRY: getAllTheatres()");
        try {
            List<Theatre> theatres = theatreRepository.findAll();
            log.info("EXIT: getAllTheatres() with {} records", theatres.size());
            return theatres;
        } catch (Exception e) {
            log.error("Error fetching all theatres", e);
            throw new ServiceException("Error retrieving getAllTheatres", e);
        }
    }

    @Override
    public Theatre getTheatreById(String id) {
        log.info("ENTRY: getTheatreById() with id: {}", id);
        Theatre theatre = theatreRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("VALIDATION FAILED: No theatre found with ID {}", id);
                    return new ResourceNotFoundException("Theatre not found with ID: " + id);
                });
        log.info("EXIT: getTheatreById() with result: {}", theatre);
        return theatre;
    }

    @Override
    public Theatre updateTheatre(String id, Theatre updated) {
        log.info("ENTRY: updateTheatre() with id: {}, data: {}", id, updated);
        Theatre theatre = theatreRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("VALIDATION FAILED: Cannot update non-existing theatre with ID {}", id);
                    return new EntityNotFoundException("Theatre not found");
                });

        theatre.setName(updated.getName());
        theatre.setAddress(updated.getAddress());
        theatre.setCity(updated.getCity());
        theatre.setState(updated.getState());
        theatre.setPincode(updated.getPincode());

        Theatre saved = theatreRepository.save(theatre);
        log.info("EXIT: updateTheatre() updated data: {}", saved);
        return saved;
    }

    @Override
    public void deleteTheatre(String id) {
        log.info("ENTRY: deleteTheatre() with id: {}", id);
        if (!theatreRepository.existsById(id)) {
            log.warn("VALIDATION FAILED: Cannot delete, theatre ID not found: {}", id);
            throw new ResourceNotFoundException("Theatre not found with ID: " + id);
        }
        theatreRepository.deleteById(id);
        log.info("EXIT: deleteTheatre() success for id: {}", id);
    }
}

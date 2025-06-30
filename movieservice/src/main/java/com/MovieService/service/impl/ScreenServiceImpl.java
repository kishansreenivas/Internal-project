package com.MovieService.service.impl;

import com.MovieService.Entity.Screen;
import com.MovieService.Entity.Theatre;
import com.MovieService.Repository.ScreenRepository;
import com.MovieService.Repository.TheatreRepository;
import com.MovieService.Services.ScreenService;
import com.MovieService.dto.ScreenRequestDto;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScreenServiceImpl implements ScreenService {

    private final ScreenRepository screenRepository;
    private final TheatreRepository theatreRepository;
    private final ModelMapper modelMapper;

    @Override
    public Screen createScreen(Screen screen) {
        log.info("ENTRY: createScreen(Screen) with data: {}", screen);
        try {
            Screen saved = screenRepository.save(screen);
            log.info("EXIT: createScreen(Screen) saved: {}", saved);
            return saved;
        } catch (Exception e) {
            log.error("ERROR: createScreen(Screen) failed", e);
            throw new ServiceException("Error creating screen", e);
        }
    }

    @Override
    public Screen createScreen(ScreenRequestDto dto) {
        log.info("ENTRY: createScreen(ScreenRequestDto) with data: {}", dto);

        Theatre theatre = theatreRepository.findById(dto.getTheatreId())
                .orElseThrow(() -> {
                    log.warn("VALIDATION FAILED: Theatre not found with ID {}", dto.getTheatreId());
                    return new EntityNotFoundException("Theatre not found");
                });

        Screen screen = modelMapper.map(dto, Screen.class);
        screen.setTheatre(theatre);  // manually set the reference

        Screen saved = screenRepository.save(screen);
        log.info("EXIT: createScreen(ScreenRequestDto) saved: {}", saved);
        return saved;
    }

    @Override
    public List<Screen> getAllScreens() {
        log.info("ENTRY: getAllScreens()");
        try {
            List<Screen> screens = screenRepository.findAll();
            log.info("EXIT: getAllScreens() - total screens: {}", screens.size());
            return screens;
        } catch (Exception e) {
            log.error("ERROR: getAllScreens() failed", e);
            throw new ServiceException("Error retrieving screens", e);
        }
    }

    @Override
    public Optional<Screen> getScreenById(String id) {
        log.info("ENTRY: getScreenById() with ID: {}", id);
        try {
            Optional<Screen> screen = screenRepository.findById(id);
            log.info("EXIT: getScreenById() result present: {}", screen.isPresent());
            return screen;
        } catch (Exception e) {
            log.error("ERROR: getScreenById() failed", e);
            throw new ServiceException("Error retrieving screen by ID", e);
        }
    }

    @Override
    public Screen updateScreen(String id, Screen updated) {
        log.info("ENTRY: updateScreen() with ID: {}, data: {}", id, updated);

        Screen screen = screenRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("VALIDATION FAILED: Screen not found with ID {}", id);
                    return new EntityNotFoundException("Screen not found");
                });

        modelMapper.map(updated, screen);

        Screen saved = screenRepository.save(screen);
        log.info("EXIT: updateScreen() updated: {}", saved);
        return saved;
    }

    @Override
    public void deleteScreen(String id) {
        log.info("ENTRY: deleteScreen() with ID: {}", id);
        try {
            screenRepository.deleteById(id);
            log.info("EXIT: deleteScreen() successful for ID: {}", id);
        } catch (Exception e) {
            log.error("ERROR: deleteScreen() failed", e);
            throw new ServiceException("Error deleting screen", e);
        }
    }
}

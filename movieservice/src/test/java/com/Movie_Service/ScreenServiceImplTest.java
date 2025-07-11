package com.Movie_Service;


import com.MovieService.Entity.Screen;
import com.MovieService.Entity.Theatre;
import com.MovieService.Repository.ScreenRepository;
import com.MovieService.Repository.TheatreRepository;
import com.MovieService.dto.ScreenRequestDto;
import com.MovieService.service.impl.ScreenServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScreenServiceImplTest {

    @Mock
    private ScreenRepository screenRepository;

    @Mock
    private TheatreRepository theatreRepository;

    @InjectMocks
    private ScreenServiceImpl screenService;

    private ModelMapper modelMapper = new ModelMapper();

    @BeforeEach
    void setUp() {
        screenService = new ScreenServiceImpl(screenRepository, theatreRepository, modelMapper);
    }

    private ScreenRequestDto getSampleRequestDto() {
        return new ScreenRequestDto(
                "Screen A",
                120,
                UUID.randomUUID().toString()
        );
    }

    private Screen getSampleScreen() {
        return new Screen(UUID.randomUUID().toString(), "Screen A", 120, new Theatre());
    }

    @Test
    void createScreen_withEntity_shouldSaveAndReturn() {
        Screen screen = getSampleScreen();
        when(screenRepository.save(screen)).thenReturn(screen);

        Screen result = screenService.createScreen(screen);

        assertNotNull(result);
        verify(screenRepository).save(screen);
    }

    @Test
    void createScreen_withDto_shouldMapAndSave() {
        ScreenRequestDto dto = getSampleRequestDto();
        Theatre theatre = new Theatre();
        theatre.setId(dto.getTheatreId());

        when(theatreRepository.findById(dto.getTheatreId())).thenReturn(Optional.of(theatre));
        when(screenRepository.save(any(Screen.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Screen result = screenService.createScreen(dto);

        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getTotalSeats(), result.getTotalSeats());
        assertEquals(theatre, result.getTheatre());
    }

    @Test
    void createScreen_withDto_shouldThrowIfTheatreNotFound() {
        ScreenRequestDto dto = getSampleRequestDto();
        when(theatreRepository.findById(dto.getTheatreId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> screenService.createScreen(dto));
    }

    @Test
    void getAllScreens_shouldReturnList() {
        when(screenRepository.findAll()).thenReturn(Arrays.asList(getSampleScreen(), getSampleScreen()));

        var result = screenService.getAllScreens();

        assertEquals(2, result.size());
        verify(screenRepository).findAll();
    }

    @Test
    void getScreenById_shouldReturnScreenIfExists() {
        Screen screen = getSampleScreen();
        when(screenRepository.findById(screen.getId())).thenReturn(Optional.of(screen));

        Optional<Screen> result = screenService.getScreenById(screen.getId());

        assertTrue(result.isPresent());
        assertEquals(screen.getId(), result.get().getId());
    }

    @Test
    void updateScreen_shouldUpdateData() {
        Screen existing = getSampleScreen();
        Screen updated = new Screen();
        updated.setName("Updated Name");
        updated.setTotalSeats(200);

        when(screenRepository.findById(existing.getId())).thenReturn(Optional.of(existing));
        when(screenRepository.save(any(Screen.class))).thenAnswer(inv -> inv.getArgument(0));

        Screen result = screenService.updateScreen(existing.getId(), updated);

        assertEquals("Updated Name", result.getName());
        assertEquals(200, result.getTotalSeats());
    }

    @Test
    void updateScreen_shouldThrowIfNotFound() {
        when(screenRepository.findById("fake-id")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> screenService.updateScreen("fake-id", new Screen()));
    }

    @Test
    void deleteScreen_shouldCallRepository() {
        doNothing().when(screenRepository).deleteById("id-123");

        screenService.deleteScreen("id-123");

        verify(screenRepository).deleteById("id-123");
    }
}

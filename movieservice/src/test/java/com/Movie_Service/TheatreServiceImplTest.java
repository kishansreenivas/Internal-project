package com.Movie_Service;


import com.MovieService.Entity.Theatre;
import com.MovieService.Exception.ResourceNotFoundException;
import com.MovieService.Repository.TheatreRepository;
import com.MovieService.service.impl.TheatreServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.hibernate.service.spi.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TheatreServiceImplTest {

    @Mock
    private TheatreRepository theatreRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    @InjectMocks
    private TheatreServiceImpl theatreService;

    private Theatre sampleTheatre;

    @BeforeEach
    void setUp() {
        sampleTheatre = new Theatre();
        sampleTheatre.setId("theatre1");
        sampleTheatre.setName("Cineplex");
        sampleTheatre.setCity("New York");
        sampleTheatre.setAddress("123 Main St");
    }

    @Test
    void createTheatre_shouldReturnSavedEntity() {
        when(theatreRepository.save(sampleTheatre)).thenReturn(sampleTheatre);

        Theatre result = theatreService.createTheatre(sampleTheatre);

        assertNotNull(result);
        assertEquals("Cineplex", result.getName());
        verify(theatreRepository).save(sampleTheatre);
    }

    @Test
    void getAllTheatres_shouldReturnList() {
        List<Theatre> theatres = List.of(sampleTheatre, new Theatre());
        when(theatreRepository.findAll()).thenReturn(theatres);

        List<Theatre> result = theatreService.getAllTheatres();

        assertEquals(2, result.size());
        verify(theatreRepository).findAll();
    }

    @Test
    void getTheatreById_shouldReturnEntity() {
        when(theatreRepository.findById("theatre1")).thenReturn(Optional.of(sampleTheatre));

        Theatre result = theatreService.getTheatreById("theatre1");

        assertEquals("Cineplex", result.getName());
        verify(theatreRepository).findById("theatre1");
    }

    @Test
    void getTheatreById_shouldThrowIfNotFound() {
        when(theatreRepository.findById("invalid-id")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> theatreService.getTheatreById("invalid-id"));
    }


    @Test
    void updateTheatre_shouldThrowIfNotFound() {
        Theatre updated = new Theatre();
        updated.setName("New Name");

        when(theatreRepository.findById("invalid-id")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> theatreService.updateTheatre("invalid-id", updated));
    }

    @Test
    void deleteTheatre_shouldDeleteIfExists() {
        when(theatreRepository.existsById("theatre1")).thenReturn(true);
        doNothing().when(theatreRepository).deleteById("theatre1");

        theatreService.deleteTheatre("theatre1");

        verify(theatreRepository).deleteById("theatre1");
    }

    @Test
    void deleteTheatre_shouldThrowIfNotExists() {
        when(theatreRepository.existsById("invalid-id")).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> theatreService.deleteTheatre("invalid-id"));
    }
}

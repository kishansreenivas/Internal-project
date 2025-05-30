package com.Movie_Service;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.MovieService.Entity.Showtime;
import com.MovieService.Services.ShowtimeService;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ShowtimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShowtimeService showtimeService;

    @Test
    public void testGetAllShowtimes() throws Exception {
        Showtime showtime = new Showtime();
        showtime.setId(1L);
        showtime.setShowStart(LocalDateTime.of(2025, 1, 1, 18, 0));
        showtime.setShowEnd(LocalDateTime.of(2025, 1, 1, 20, 0));
        showtime.setLanguage("English");

        Mockito.when(showtimeService.getAllShowtimes()).thenReturn(Collections.singletonList(showtime));

        mockMvc.perform(get("/showtimes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].language").value("English"));
    }
}

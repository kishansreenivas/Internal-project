package com.Movie_Service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.MovieService.Entity.Screen;
import com.MovieService.Entity.Theatre;
import com.MovieService.Services.ScreenService;

@SpringBootTest
@AutoConfigureMockMvc
public class ScreenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScreenService screenService;

    @Test
    public void testGetAllScreens() throws Exception {
        Theatre theatre = new Theatre();
        theatre.setId(String.valueOf(123L));
        theatre.setName("PVR");

        Screen screen = new Screen();
        screen.setId(String.valueOf(123L));
        screen.setName("Screen 1");
        screen.setTotalSeats(150);
        screen.setTheatre(theatre);

        Mockito.when(screenService.getAllScreens()).thenReturn(Collections.singletonList(screen));

        mockMvc.perform(get("/screens"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Screen 1"))
                .andExpect(jsonPath("$[0].totalSeats").value(150));
    }
}

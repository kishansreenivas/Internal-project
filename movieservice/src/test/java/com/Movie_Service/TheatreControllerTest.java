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

import com.MovieService.Entity.Theatre;
import com.MovieService.Services.TheatreService;

@SpringBootTest
@AutoConfigureMockMvc
public class TheatreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TheatreService theatreService;

    @Test
    public void testGetAllTheatres() throws Exception {
        Theatre theatre = new Theatre();
        theatre.setId(String.valueOf(123L));
        theatre.setName("PVR Cinema");
        theatre.setCity("Mumbai");

        Mockito.when(theatreService.getAllTheatres()).thenReturn(Collections.singletonList(theatre));

        mockMvc.perform(get("/theatres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("PVR Cinema"))
                .andExpect(jsonPath("$[0].city").value("Mumbai"));
    }
}

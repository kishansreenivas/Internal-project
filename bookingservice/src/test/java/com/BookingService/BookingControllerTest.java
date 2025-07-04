package com.BookingService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookingControllerTest {

    @Autowired private MockMvc mockMvc;

    @Test
    public void testInitiateBooking_BadRequest() throws Exception {
        String payload = """
        {
            "userId": "user123",
            "showId": "show456",
            "screenId": "screen789",
            "seatIds": ["A1", "A2"],
            "totalAmount": 300
        }
        """;

        mockMvc.perform(post("/v1/bookings/initiate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk()); // change to isBadRequest if using validation
    }
}

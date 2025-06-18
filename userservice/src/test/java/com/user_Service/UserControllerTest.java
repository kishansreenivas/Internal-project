package com.user_Service;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.UserService.Controller.UserController;
import com.UserService.Dto.UserDTO;
import com.UserService.Services.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void testGetUserById() throws Exception {
        UUID userId = UUID.randomUUID();

        UserDTO dto = new UserDTO();
        dto.setId(userId);
        dto.setFirstName("John");

        Mockito.when(userService.getUserById(userId)).thenReturn(dto);

        mockMvc.perform(get("/api/users/" + userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }
}

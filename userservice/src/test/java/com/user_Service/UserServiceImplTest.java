package com.user_Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.UserService.Dto.UserDTO;
import com.UserService.Entity.User;
import com.UserService.Exception.ResourceNotFoundException;
import com.UserService.Mapper.UserMapper;
import com.UserService.Repositories.UserRepository;
import com.UserService.Servicesimpl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testCreateUser() {
        UserDTO dto = new UserDTO();
        dto.setFirstName("John");

        User user = new User();
        user.setFirstName("John");

        Mockito.when(userMapper.toEntity(dto)).thenReturn(user);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userMapper.toDTO(user)).thenReturn(dto);

        UserDTO result = userService.createUser(dto);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
    }

    @Test
    void testGetUserById_notFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(userId));
    }
}

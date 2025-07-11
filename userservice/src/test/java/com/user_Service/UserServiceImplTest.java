package com.user_Service;

import com.UserService.Dto.UserDTO;
import com.UserService.Entity.User;
import com.UserService.Exception.ResourceNotFoundException;
import com.UserService.Mapper.UserMapper;
import com.UserService.Repositories.UserRepository;
import com.UserService.Servicesimpl.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepo;

    @Mock
    private UserMapper mapper;

    private User sampleUser;
    private UserDTO sampleUserDto;

    @BeforeEach
    public void setup() {
        UUID id = UUID.randomUUID();
        sampleUser = new User();
        sampleUser.setId(id);
        sampleUser.setFirstName("John");
        sampleUser.setLastName("Doe");
        sampleUser.setEmail("john@example.com");
        sampleUser.setPhone("1234567890");

        sampleUserDto = new UserDTO();
        sampleUserDto.setId(id);
        sampleUserDto.setFirstName("John");
        sampleUserDto.setLastName("Doe");
        sampleUserDto.setEmail("john@example.com");
        sampleUserDto.setPhone("1234567890");
    }

    // Test for createUser
    @Test
    public void testCreateUser_Success() {
        // Arrange
        when(mapper.toEntity(sampleUserDto)).thenReturn(sampleUser);
        when(userRepo.save(any(User.class))).thenReturn(sampleUser);
        when(mapper.toDTO(sampleUser)).thenReturn(sampleUserDto);

        // Act
        UserDTO created = userService.createUser(sampleUserDto);

        // Assert
        assertNotNull(created);
        assertEquals("john@example.com", created.getEmail());
        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    public void testCreateUser_NullInput_ThrowsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(null);
        });

        assertEquals("User data cannot be null", exception.getMessage());
    }

    // Test for deleteUser
    @Test
    public void testDeleteUser_Success() {
        UUID userId = sampleUser.getId();

        when(userRepo.findById(userId)).thenReturn(Optional.of(sampleUser));

        userService.deleteUser(userId);

        verify(userRepo, times(1)).delete(sampleUser);
    }

    @Test
    public void testDeleteUser_NotFound_ThrowsException() {
        UUID nonExistentId = UUID.randomUUID();

        when(userRepo.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.deleteUser(nonExistentId);
        });

        verify(userRepo, never()).delete(any());
    }
}

package com.NotificationService;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.*;

import com.NotificationService.External.Service.UserServiceClient;
import com.NotificationService.dto.*;
import com.NotificationService.entity.Notification;
import com.NotificationService.enums.NotificationStatus;
import com.NotificationService.enums.NotificationType;
import com.NotificationService.exception.ResourceNotFoundException;
import com.NotificationService.repository.NotificationRepository;
import com.NotificationService.sender.EmailSender;
import com.NotificationService.sender.SmsSender;
import com.NotificationService.service.impl.NotificationServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
public class NotificationServiceImplTest {

    @Autowired
    private NotificationServiceImpl notificationService;

    @MockBean
    private NotificationRepository repository;

    @MockBean
    private EmailSender emailSender;

    @MockBean
    private SmsSender smsSender;

    @MockBean
    private UserServiceClient userServiceClient;

    @Autowired
    private ModelMapper modelMapper;

    private NotificationRequestDTO requestDTO;
    private UserContactDTO contactDTO;
    private Notification notification;

    @BeforeEach
    void setup() {
        requestDTO = new NotificationRequestDTO();
        requestDTO.setUserId("12345");
        requestDTO.setSubject("Test Subject");
        requestDTO.setContent("Test Content");
        requestDTO.setType(NotificationType.EMAIL);
        
       notification = new Notification();
        notification.setId(1L);
        notification.setRecipient("test@example.com");
        notification.setSubject("Test Subject");
        notification.setContent("Test Content");
        notification.setType(NotificationType.EMAIL);
        notification.setStatus(NotificationStatus.SENT);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setSentAt(LocalDateTime.now());
    }


    @Test
    void testGetNotificationById_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(notification));

        NotificationResponseDTO response = notificationService.getNotificationById(1L);

        assertEquals(NotificationStatus.SENT, response.getStatus());
        assertEquals("test@example.com", response.getRecipient());
    }

    @Test
    void testGetNotificationById_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            notificationService.getNotificationById(1L);
        });
    }

    @Test
    void testDeleteNotificationById_Success() {
        when(repository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> notificationService.deleteNotificationById(1L));
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNotificationById_NotFound() {
        when(repository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            notificationService.deleteNotificationById(1L);
        });
    }

    @Test
    void testGetAllNotifications() {
        when(repository.findAll()).thenReturn(List.of(notification));

        List<NotificationResponseDTO> list = notificationService.getAllNotifications();

        assertEquals(1, list.size());
        assertEquals("test@example.com", list.get(0).getRecipient());
    }

    @Test
    void testGetAllNotificationsPaginated() {
        PageRequest pageable = PageRequest.of(0, 5);
        Page<Notification> page = new PageImpl<>(List.of(notification), pageable, 1);
        when(repository.findAll(pageable)).thenReturn(page);

        Page<NotificationResponseDTO> result = notificationService.getAllNotificationsPaginated(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("test@example.com", result.getContent().get(0).getRecipient());
    }
}

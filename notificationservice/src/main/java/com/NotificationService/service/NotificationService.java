package com.NotificationService.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.NotificationService.dto.NotificationRequestDTO;
import com.NotificationService.dto.NotificationResponseDTO;

public interface NotificationService {
    NotificationResponseDTO sendNotification(NotificationRequestDTO request);
    NotificationResponseDTO getNotificationById(Long id);
    List<NotificationResponseDTO> getAllNotifications();
    void deleteNotificationById(Long id);
    Page<NotificationResponseDTO> getAllNotificationsPaginated(Pageable pageable);

}

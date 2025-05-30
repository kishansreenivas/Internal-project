package com.NotificationService.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.NotificationService.dto.NotificationRequestDTO;
import com.NotificationService.dto.NotificationResponseDTO;
import com.NotificationService.entity.Notification;
import com.NotificationService.enums.NotificationStatus;
import com.NotificationService.enums.NotificationType;
import com.NotificationService.exception.ResourceNotFoundException;
import com.NotificationService.repository.NotificationRepository;
import com.NotificationService.sender.EmailSender;
import com.NotificationService.sender.SmsSender;

@Service
public class NotificationServiceImpl implements NotificationService 
{

    @Autowired
    private NotificationRepository repository;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private SmsSender smsSender;

    @Override
    public NotificationResponseDTO sendNotification(NotificationRequestDTO request) {
        Notification notification = mapToEntity(request);
        notification.setStatus(NotificationStatus.PENDING);
        notification.setCreatedAt(LocalDateTime.now());
        repository.save(notification);

        try {
            System.out.println("Attempting to send " + request.getType());

            if (request.getType() == NotificationType.EMAIL) {
                emailSender.sendEmail(request.getRecipient(), request.getSubject(), request.getContent());
            } else {
                smsSender.sendSms(request.getRecipient(), request.getContent());
            }

            System.out.println("Sent successfully!");
            notification.setStatus(NotificationStatus.SENT);
            notification.setSentAt(LocalDateTime.now());

        } catch (Exception ex) {
            System.err.println("Failed to send notification: " + ex.getMessage());
            ex.printStackTrace(); // Logs full stack trace
            notification.setStatus(NotificationStatus.FAILED);
        }

        repository.save(notification);
        return mapToResponse(notification);
    }


    // ✅ Mapping DTO to Entity
    private Notification mapToEntity(NotificationRequestDTO dto) {
        Notification notification = new Notification();
        notification.setUserId(dto.getUserId());
        notification.setRecipient(dto.getRecipient());
        notification.setSubject(dto.getSubject());
        notification.setContent(dto.getContent());
        notification.setType(dto.getType());
        return notification;
    }

    // ✅ Mapping Entity to DTO
    private NotificationResponseDTO mapToResponse(Notification notification) {
        NotificationResponseDTO response = new NotificationResponseDTO();
        response.setId(notification.getId());
        response.setStatus(notification.getStatus());

        if (notification.getStatus() == NotificationStatus.SENT) {
            response.setMessage("Notification sent successfully.");
        } else if (notification.getStatus() == NotificationStatus.PENDING) {
            response.setMessage("Notification is pending delivery.");
        } else {
            response.setMessage("Notification failed to send. Check server logs.");
        }

        return response;
    }

    
    @Override
    public NotificationResponseDTO getNotificationById(Long id) {
        Notification notification = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with ID: " + id));
        return mapToResponse(notification);
    }

    @Override
    public List<NotificationResponseDTO> getAllNotifications() {
        List<Notification> notifications = repository.findAll();
        return notifications.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteNotificationById(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Notification not found with ID: " + id);
        }
        repository.deleteById(id);
    }

}

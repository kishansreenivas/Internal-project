package com.NotificationService.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.NotificationService.External.Service.UserServiceClient;
import com.NotificationService.dto.NotificationRequestDTO;
import com.NotificationService.dto.NotificationResponseDTO;
import com.NotificationService.dto.UserContactDTO;
import com.NotificationService.entity.Notification;
import com.NotificationService.enums.NotificationStatus;
import com.NotificationService.enums.NotificationType;
import com.NotificationService.exception.ResourceNotFoundException;
import com.NotificationService.repository.NotificationRepository;
import com.NotificationService.sender.EmailSender;
import com.NotificationService.sender.SmsSender;
import com.NotificationService.service.NotificationService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    @Autowired private NotificationRepository repository;
    @Autowired private EmailSender emailSender;
    @Autowired private SmsSender smsSender;
    @Autowired private UserServiceClient userServiceClient;
    @Autowired private ModelMapper modelMapper;

    /**
     * Configure ModelMapper to avoid mapping String userId to Long id
     */
    @PostConstruct
    public void configureModelMapper() {
        modelMapper.addMappings(new PropertyMap<NotificationRequestDTO, Notification>() {
            @Override
            protected void configure() {
                skip(destination.getId()); // Prevent conflict with Notification's Long ID
            }
        });
    }

    @Override
    public NotificationResponseDTO sendNotification(NotificationRequestDTO request) {
        log.info("Received sendNotification request for userId: {}", request.getUserId());

        if (request.getUserId() == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }

        // Fetch contact details using Feign Client
        UserContactDTO contact = userServiceClient.getUserContact(request.getUserId());
        log.debug("Fetched user contact: {}", contact);

        String recipient = (request.getType() == NotificationType.EMAIL)
                ? contact.getEmail()
                : contact.getPhone();

        Notification notification;
        try {
            notification = modelMapper.map(request, Notification.class);
        } catch (Exception e) {
            log.error("Failed to map NotificationRequestDTO to Notification", e);
            throw new RuntimeException("Mapping failed", e);
        }

        notification.setRecipient(recipient);
        notification.setStatus(NotificationStatus.PENDING);
        notification.setCreatedAt(LocalDateTime.now());
        repository.save(notification);

        try {
            if (request.getType() == NotificationType.EMAIL) {
                log.info("Sending email to {}", recipient);
                emailSender.sendEmail(recipient, request.getSubject(), request.getContent());
            } else {
                log.info("Sending SMS to {}", recipient);
                smsSender.sendSms(recipient, request.getContent());
            }

            notification.setStatus(NotificationStatus.SENT);
            notification.setSentAt(LocalDateTime.now());
            log.info("Notification sent successfully for ID: {}", notification.getId());

        } catch (Exception ex) {
            log.error("Sending failed for notification ID {}: {}", notification.getId(), ex.getMessage(), ex);
            notification.setStatus(NotificationStatus.FAILED);
        }

        repository.save(notification);
        return mapToResponse(notification);
    }

    private NotificationResponseDTO mapToResponse(Notification notification) {
        NotificationResponseDTO response = modelMapper.map(notification, NotificationResponseDTO.class);
        response.setMessage(switch (notification.getStatus()) {
            case SENT -> "Notification sent successfully.";
            case PENDING -> "Notification is pending delivery.";
            default -> "Notification failed to send. Check server logs.";
        });
        return response;
    }

   
    @Override
    public NotificationResponseDTO getNotificationById(Long id) {
        log.info("Fetching notification with ID: {}", id);

        Notification notification = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Notification not found with ID: {}", id);
                    return new ResourceNotFoundException("Notification not found with ID: " + id);
                });

        log.debug("Notification fetched: {}", notification);
        return mapToResponse(notification);
    }

    @Override
    public List<NotificationResponseDTO> getAllNotifications() {
        log.info("Fetching all notifications");

        List<NotificationResponseDTO> notifications = repository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        log.info("Total notifications fetched: {}", notifications.size());
        return notifications;
    }

    @Override
    public void deleteNotificationById(Long id) {
        log.info("Deleting notification with ID: {}", id);

        if (!repository.existsById(id)) {
            log.warn("Cannot delete; notification not found with ID: {}", id);
            throw new ResourceNotFoundException("Notification not found with ID: " + id);
        }

        repository.deleteById(id);
        log.info("Deleted notification with ID: {}", id);
    }

    @Override
    public Page<NotificationResponseDTO> getAllNotificationsPaginated(Pageable pageable) {
        log.info("Fetching paginated notifications: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());

        Page<NotificationResponseDTO> page = repository.findAll(pageable)
                .map(this::mapToResponse);

        log.info("Fetched {} notifications on current page", page.getNumberOfElements());
        return page;
    }

} 

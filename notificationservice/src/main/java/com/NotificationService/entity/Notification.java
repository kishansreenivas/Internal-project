package com.NotificationService.entity;

import java.time.LocalDateTime;

import com.NotificationService.enums.NotificationStatus;
import com.NotificationService.enums.NotificationType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String recipient; // email or phone
    private String subject;
    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    private NotificationType type; 
    @Enumerated(EnumType.STRING)
    private NotificationStatus status; 

    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
}

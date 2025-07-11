package com.NotificationService.dto;

import com.NotificationService.enums.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class NotificationRequestDTO {
    private String userId;
    private String recipient;
    private String subject;
    private String content;
    private NotificationType type;
}


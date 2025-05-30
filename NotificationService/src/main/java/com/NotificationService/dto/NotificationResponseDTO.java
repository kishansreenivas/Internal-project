package com.NotificationService.dto;

import com.NotificationService.enums.NotificationStatus;

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
public class NotificationResponseDTO {
    private Long id;
    private NotificationStatus status;
    private String message;
}

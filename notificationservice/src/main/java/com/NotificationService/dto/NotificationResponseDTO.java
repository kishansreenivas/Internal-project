package com.NotificationService.dto;

import java.time.LocalDateTime;

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
    private String userId;
    private String message;
    private String recipient;
    private String subject;
    private String content;
    private NotificationStatus status;
    public NotificationResponseDTO(Long id,String userId, String recipient, String subject, String content, NotificationStatus status,
			LocalDateTime sentAt) {
		super();
		this.id = id;
		this.userId=userId;
		this.recipient = recipient;
		this.subject = subject;
		this.content = content;
		this.status = status;
		this.sentAt = sentAt;
	}
	private LocalDateTime sentAt;

}

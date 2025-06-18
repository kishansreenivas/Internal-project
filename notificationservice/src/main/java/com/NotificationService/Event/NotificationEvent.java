package com.NotificationService.Event;

import org.springframework.context.ApplicationEvent;

import com.NotificationService.dto.NotificationRequestDTO;

public class NotificationEvent extends ApplicationEvent {
    public NotificationEvent(NotificationRequestDTO dto) {
        super(dto);
    }

    public NotificationRequestDTO getNotification() {
        return (NotificationRequestDTO) getSource();
    }
}

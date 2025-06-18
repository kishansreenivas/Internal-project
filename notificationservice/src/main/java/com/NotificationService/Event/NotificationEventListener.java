package com.NotificationService.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.NotificationService.dto.NotificationRequestDTO;
import com.NotificationService.service.NotificationService;

@Component
public class NotificationEventListener {

    @Autowired private NotificationService service;

    @Async
    @EventListener
    public void handleNotification(NotificationEvent event) {
        NotificationRequestDTO dto = event.getNotification();
        service.sendNotification(dto);
    }
}
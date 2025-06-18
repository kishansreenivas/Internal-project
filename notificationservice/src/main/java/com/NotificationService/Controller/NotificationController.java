package com.NotificationService.Controller;

import com.NotificationService.Event.NotificationEvent;
import com.NotificationService.dto.NotificationRequestDTO;
import com.NotificationService.dto.NotificationResponseDTO;

import com.NotificationService.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/notifications")
public class NotificationController {

    @Autowired private NotificationService service;
    @Autowired private ApplicationEventPublisher publisher;

    // ✅ Event-driven notification sending
    @PostMapping("/async")
    public ResponseEntity<String> publishEvent(@RequestBody NotificationRequestDTO dto) {
        publisher.publishEvent(new NotificationEvent(dto));
        return ResponseEntity.ok("Notification published for async processing.");
    }

    // ✅ Direct synchronous sending
    @PostMapping
    public ResponseEntity<NotificationResponseDTO> send(@RequestBody NotificationRequestDTO dto) {
        return ResponseEntity.ok(service.sendNotification(dto));
    }

    // ✅ Get notification by ID
    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getNotificationById(id));
    }

    // ✅ Get all notifications (optional: paginated or just list)
    @GetMapping
    public ResponseEntity<List<NotificationResponseDTO>> getAll() {
        Pageable pageable = PageRequest.of(0, 100); // Max limit or configurable
        Page<NotificationResponseDTO> page = service.getAllNotificationsPaginated(pageable);
        return ResponseEntity.ok(page.getContent());
    }

    // ✅ Delete notification by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        service.deleteNotificationById(id);
        return ResponseEntity.ok("Notification deleted successfully.");
    }
}
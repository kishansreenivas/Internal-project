package com.NotificationService.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.NotificationService.dto.NotificationRequestDTO;
import com.NotificationService.dto.NotificationResponseDTO;
import com.NotificationService.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService service;

    // Send new notification
    @PostMapping
    public ResponseEntity<NotificationResponseDTO> send(@RequestBody NotificationRequestDTO dto) {
        return ResponseEntity.ok(service.sendNotification(dto));
    }

    // ✅ Get notification by ID
    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getNotificationById(id));
    }

    // ✅ Get all notifications (Optional: paginated)
    @GetMapping
    public ResponseEntity<List<NotificationResponseDTO>> getAll() {
        return ResponseEntity.ok(service.getAllNotifications());
    }

    // ✅ Delete notification by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        service.deleteNotificationById(id);
        return ResponseEntity.ok("Notification deleted successfully.");
    }
}

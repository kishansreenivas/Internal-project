package com.NotificationService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.NotificationService.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}


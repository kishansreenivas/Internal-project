package com.NotificationService.sender;

import org.springframework.stereotype.Component;

@Component
public class SmsSender {
    public void sendSms(String to, String text) {
        // Integrate with SMS Gateway like Twilio or mock it
        System.out.println("SMS sent to " + to + ": " + text);
    }
}

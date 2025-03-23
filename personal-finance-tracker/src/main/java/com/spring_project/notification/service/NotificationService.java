package com.spring_project.notification.service;

import com.spring_project.notification.client.NotificationClient;
import com.spring_project.notification.client.dto.NotificationPreference;
import com.spring_project.notification.client.dto.NotificationRequest;
import com.spring_project.notification.client.dto.UpsertPreference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class NotificationService {

    private final NotificationClient notificationClient;

    public NotificationService(NotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }

    public void savePreference(UUID userId, boolean enabled, String email) {

        UpsertPreference preference = UpsertPreference.builder()
                .userId(userId)
                .enabled(enabled)
                .type("EMAIL")
                .contactInfo(email)
                .build();

        ResponseEntity<Void> response = notificationClient.upsertPreference(preference);
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("Failed to upsert preference");
        }
    }


    public NotificationPreference getNotificationPreference(UUID userId) {

        ResponseEntity<NotificationPreference> response = notificationClient.getUserPreference(userId);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to retrieve preference");
        }
        return response.getBody();
    }

    public void sendEmail(UUID userId, String emailTitle, String body) {

        NotificationRequest notificationRequest = NotificationRequest.builder()
                .userId(userId)
                .title(emailTitle)
                .body(body)
                .build();


        ResponseEntity<Void> response = notificationClient.sendNotification(notificationRequest);

        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("Failed to send email");
        }

    }
}

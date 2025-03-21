package com.spring_project.notification.client;

import com.spring_project.notification.client.dto.NotificationPreference;
import com.spring_project.notification.client.dto.NotificationRequest;
import com.spring_project.notification.client.dto.UpsertPreference;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.management.Notification;
import java.util.UUID;

@FeignClient(name = "email", url = "http://localhost:8081/api/v1/notifications")
public interface NotificationClient {

    @PostMapping("/preferences")
    ResponseEntity<Void> upsertPreference(@RequestBody UpsertPreference upsertPreference);


    @GetMapping("/preferences")
    ResponseEntity<NotificationPreference> getUserPreference(@RequestParam("userId") UUID userId);


    @PostMapping
    ResponseEntity<Void> sendNotification(@RequestBody NotificationRequest notificationRequest);

}

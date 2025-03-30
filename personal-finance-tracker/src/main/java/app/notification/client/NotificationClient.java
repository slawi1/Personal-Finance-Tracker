package app.notification.client;

import app.notification.client.dto.NotificationPreference;
import app.notification.client.dto.NotificationRequest;
import app.notification.client.dto.UpsertPreference;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/preferences")
    ResponseEntity<NotificationPreference> updateNotification(@RequestParam ("userId") UUID userId, @RequestParam ("enabled") boolean enabled);

}

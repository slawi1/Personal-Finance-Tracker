package app.web;

import app.notification.client.dto.NotificationPreference;
import app.notification.service.NotificationService;
import app.security.AuthenticationData;
import app.user.model.User;
import app.user.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    private final UserService userService;
    private final NotificationService notificationService;

    public NotificationController(UserService userService, NotificationService notificationService) {
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @GetMapping
    public ModelAndView getNotifications(@AuthenticationPrincipal AuthenticationData authenticationData) {

        User user = userService.getById(authenticationData.getId());

        NotificationPreference notificationPreference = notificationService.getNotificationPreference(user.getId());

        ModelAndView modelAndView = new ModelAndView("notifications");
        modelAndView.addObject("user", user);
        modelAndView.addObject("notificationPreference", notificationPreference);

        return modelAndView;
    }

    @PutMapping("preferences")
    public String updateNotification(@AuthenticationPrincipal AuthenticationData authenticationData, @RequestParam ("enabled") boolean enabled) {
        notificationService.updatePreference(authenticationData.getId(), enabled);

        return "redirect:/notifications";
    }
}

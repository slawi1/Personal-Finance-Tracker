package com.spring_project.web;

import com.spring_project.notification.client.dto.NotificationPreference;
import com.spring_project.notification.service.NotificationService;
import com.spring_project.security.AuthenticationData;
import com.spring_project.user.model.User;
import com.spring_project.user.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
}

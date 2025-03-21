package com.spring_project.notification.client.dto;

import lombok.Data;

@Data
public class NotificationPreference {

    private String type;

    private boolean enabled;

    private String contactInfo;
}

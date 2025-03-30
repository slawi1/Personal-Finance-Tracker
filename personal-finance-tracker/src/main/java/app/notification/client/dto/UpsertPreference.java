package app.notification.client.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UpsertPreference {

    private UUID userId;

    private boolean enabled;

    private String type;

    private String contactInfo;

}

package com.spring_project.web.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
@Builder
public class EditProfileRequest {

    private String firstName;

    private String lastName;

    @URL
    private String profilePicture;


}

package com.spring_project.web.mapper;

import com.spring_project.user.model.User;
import com.spring_project.web.dto.EditProfileRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {

    public static EditProfileRequest mapUserToEditProfileRequest(User user) {

        return EditProfileRequest.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .profilePicture(user.getProfilePicture())
                .build();
    }
}

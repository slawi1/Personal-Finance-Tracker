package com.spring_project.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @Size(min = 4, max = 40, message = "Username must be between 4 and 40 characters")
    private String username;

    @Email(message = "Enter a valid email")
    @NotBlank
    private String email;

    @Size(min = 4, message = "Minimum 4 characters")
    private String password;

    @Size(min = 4, message = "Minimum 4 characters")
    private String confirmPassword;

}

package app.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Username cannot be blank!")
    @Size(min = 6, max = 40, message = "Username must be between 6 and 40 characters!")
    private String username;

    @Email(message = "Enter a valid email!")
    @NotBlank(message = "Email cannot be blank!")
    private String email;

    @Size(min = 8, message = "Minimum 8 characters")
    private String password;

    @Size(min = 8, message = "Minimum 8 characters")
    private String confirmPassword;

}

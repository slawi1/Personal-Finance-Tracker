package app.web.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

    @Size(min = 6, max = 40, message = "Username must be 6 symbols or more")
    private String username;

    @Size(min = 8, message = "Minimum 8 characters")
    private String password;
}

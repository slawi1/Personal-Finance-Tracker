package app.web.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddCategoryRequest {

    @Size(min = 3, message = "Minimum 3 characters")
    private String categoryName;
}

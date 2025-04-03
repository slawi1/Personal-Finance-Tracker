package app.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddCategoryRequest {

    @Size(min = 3, max = 40, message = "Category name must be between 3 and 40 characters!")
    @NotBlank(message = "Category name cannot be blank!")
    private String categoryName;
}

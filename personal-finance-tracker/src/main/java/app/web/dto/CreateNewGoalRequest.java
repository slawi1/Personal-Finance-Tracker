package app.web.dto;

import app.user.model.User;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateNewGoalRequest {

    @NotBlank(message = "Goal name cannot be blank!")
    @Size(min = 4, message = "Goal name must be at least 4 characters long!")
    private String goalName;

    private User owner;

    @NotNull(message = "Amount cannot be null!")
    @Positive(message = "Only positive numbers allowed!")
    private BigDecimal targetAmount;

    @FutureOrPresent(message = "Deadline cannot be in the past!")
    @NotNull(message = "Deadline cannot be null!")
    private LocalDate deadline;
}

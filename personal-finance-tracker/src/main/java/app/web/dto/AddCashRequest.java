package app.web.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddCashRequest {

    @Size(min = 4, message = "At least 4 characters long!")
    @NotBlank(message = "Field cannot be blank!")
    private String sourceOfIncome;

    private String description;

    @PastOrPresent(message = "Date cannot be in the future!")
    @NotNull(message = "Date cannot be null!")
    private LocalDate date;

    private UUID category;

    @Positive(message = "Only positive numbers!")
    @NotNull(message = "Amount cannot be null!")
    private BigDecimal amount;
}

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
@NoArgsConstructor
@AllArgsConstructor
public class AddExpenseRequest {


    @NotBlank(message = "Transaction name cannot be blank!")
    @Size(min = 4, max = 50, message = "Transaction name must be between 4 and 50 characters long!")
    private String transactionName;

    @NotNull(message = "Amount cannot be null!")
    @Positive(message = "Amount must be positive number")
    private BigDecimal amount;

    private UUID category;
    @NotNull(message = "Transaction date cannot be null!")
    @PastOrPresent(message = "Transaction date cannot be in the future")
    private LocalDate transactionDate;

    private String description;

}

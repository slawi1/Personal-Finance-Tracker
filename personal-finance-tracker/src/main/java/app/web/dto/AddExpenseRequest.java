package app.web.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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


    @Size
    private String transactionName;

    @Positive
    private BigDecimal amount;

    private UUID category;

    private LocalDate transactionDate;

    private String description;

}

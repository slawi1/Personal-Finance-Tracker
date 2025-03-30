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
@AllArgsConstructor
@NoArgsConstructor
public class AddCashRequest {

    @Size(min = 6, message = "At least 6 characters long")
    private String sourceOfIncome;


    private String description;

    private LocalDate date;

    private UUID category;

    @Positive
    private BigDecimal amount;
}

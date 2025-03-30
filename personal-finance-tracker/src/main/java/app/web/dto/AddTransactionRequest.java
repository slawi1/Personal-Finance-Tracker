package app.web.dto;

import app.transaction.model.Type;
import app.user.model.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddTransactionRequest {

    @NotBlank
    @Size(min = 4, max = 100,message = "Between 4 and 100 symbols")
    private String transactionName;

    @Positive
    private BigDecimal amount;

    private UUID category;

    private User owner;

    @Enumerated(EnumType.STRING)
    private Type transactionType;

    private LocalDate transactionDate;

    @NotBlank
    private String description;



}

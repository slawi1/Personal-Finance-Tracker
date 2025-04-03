package app.web.dto;

import app.recurringPayment.model.Frequency;
import app.user.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class RecurringPaymentRequest {

    @NotBlank(message = "Field cannot be blank!")
    @Size(min = 4, max = 60, message = "Between 4 and 60 characters!")
    private String paymentName;

    private User owner;

    @NotNull(message = "Amount cannot be null!")
    @Positive(message = "Only positive numbers allowed!")
    private BigDecimal amount;

    @NotNull(message = "Payment date cannot be null!")
    private LocalDate paymentDate;

    private UUID categoryId;

    private Frequency frequency;

    private String description;
}

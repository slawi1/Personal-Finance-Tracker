package app.web.dto;

import app.recurringPayment.model.Frequency;
import app.user.model.User;
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

    private String paymentName;

    private User owner;

    private BigDecimal amount;

    private LocalDate paymentDate;

    private UUID categoryId;

    private Frequency frequency;

    private String description;
}

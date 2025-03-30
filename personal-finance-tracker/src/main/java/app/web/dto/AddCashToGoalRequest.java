package app.web.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Getter
@Setter
public class AddCashToGoalRequest {

    @Positive
    private BigDecimal amount;

    private UUID goalId;
}

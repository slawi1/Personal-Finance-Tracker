package app.savingsGoal.model;

import app.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavingsGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String goalName;

    @ManyToOne
    @JoinColumn(name = "goalOwner_id",nullable = false)
    private User goalOwner;

    private BigDecimal targetAmount;

    private BigDecimal currentAmount;

    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    private Status status;

}

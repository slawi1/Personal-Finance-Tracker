package com.spring_project.savingsGoal.model;

import com.spring_project.user.model.User;
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
    private User goalOwner;

    private BigDecimal targetAmount;

    private BigDecimal currentAmount;

    private LocalDate deadline;

    private Status status;

}

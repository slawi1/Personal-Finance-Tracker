package com.spring_project.recurringPayment.model;

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
public class RecurringPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private User owner;

    private BigDecimal amount;

    private UUID categoryId;

    private LocalDate paymentDate;

    @Enumerated(EnumType.STRING)
    private Frequency frequency;

    private String description;


}

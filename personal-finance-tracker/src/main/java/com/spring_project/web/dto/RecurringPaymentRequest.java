package com.spring_project.web.dto;

import com.spring_project.recurringPayment.model.Frequency;
import com.spring_project.user.model.User;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class RecurringPaymentRequest {

    private String paymentName;

    private User owner;

    private BigDecimal amount;

    private LocalDate paymentDate;

    private UUID categoryId;

    private Frequency frequency;

    private String description;
}

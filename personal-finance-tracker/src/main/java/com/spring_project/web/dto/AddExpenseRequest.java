package com.spring_project.web.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class AddExpenseRequest {


    @Size
    private String transactionName;

    @Positive
    private BigDecimal amount;

    private UUID category;

    private LocalDate transactionDate;

    private String description;

}

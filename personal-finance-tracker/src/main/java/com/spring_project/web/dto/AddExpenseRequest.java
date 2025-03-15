package com.spring_project.web.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class AddExpenseRequest {


    private String transactionName;

    private BigDecimal amount;

    private UUID category;

    private LocalDate transactionDate;

}

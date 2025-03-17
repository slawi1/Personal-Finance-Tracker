package com.spring_project.web.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class AddCashRequest {

    @Size(min = 6, message = "At least 6 characters long")
    private String sourceOfIncome;


    private String description;

    private LocalDate date;

    private UUID category;

    @Positive
    private BigDecimal amount;
}

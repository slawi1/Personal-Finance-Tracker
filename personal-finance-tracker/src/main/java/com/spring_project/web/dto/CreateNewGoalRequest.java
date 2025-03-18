package com.spring_project.web.dto;

import com.spring_project.user.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreateNewGoalRequest {

    @NotBlank
    private String goalName;

    private User owner;

    @Positive
    private BigDecimal targetAmount;

    private LocalDate deadline;
}

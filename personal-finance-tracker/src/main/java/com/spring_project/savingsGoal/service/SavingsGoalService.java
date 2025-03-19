package com.spring_project.savingsGoal.service;

import com.spring_project.savingsGoal.model.SavingsGoal;
import com.spring_project.savingsGoal.model.Status;
import com.spring_project.savingsGoal.repository.SavingsGoalRepository;
import com.spring_project.transaction.service.TransactionService;
import com.spring_project.user.model.User;
import com.spring_project.user.service.UserService;
import com.spring_project.web.dto.AddCashToGoalRequest;
import com.spring_project.web.dto.CreateNewGoalRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class SavingsGoalService {

    private final SavingsGoalRepository savingsGoalRepository;
    private final UserService userService;
    private final TransactionService transactionService;

    public SavingsGoalService(SavingsGoalRepository savingsGoalRepository, UserService userService, TransactionService transactionService) {
        this.savingsGoalRepository = savingsGoalRepository;
        this.userService = userService;
        this.transactionService = transactionService;
    }

    public void createNewGoal(CreateNewGoalRequest createNewGoalRequest, User user) {

        SavingsGoal newGoal = SavingsGoal.builder()
                .goalName(createNewGoalRequest.getGoalName())
                .goalOwner(user)
                .targetAmount(createNewGoalRequest.getTargetAmount())
                .currentAmount(BigDecimal.ZERO)
                .deadline(createNewGoalRequest.getDeadline())
                .status(Status.ACTIVE)
                .build();

        savingsGoalRepository.save(newGoal);

    }
    @Transactional
    public void addCashToGoal(AddCashToGoalRequest addCashToGoalRequest, User user) {

        Optional<SavingsGoal> goal = savingsGoalRepository.findById(addCashToGoalRequest.getGoalId());

        if (goal.isPresent()) {

            SavingsGoal savingsGoal = goal.get();
            savingsGoal.setCurrentAmount(savingsGoal.getCurrentAmount().add(addCashToGoalRequest.getAmount()));
            if (savingsGoal.getCurrentAmount().equals(savingsGoal.getTargetAmount())) {
                savingsGoal.setStatus(Status.COMPLETED);
            }
            savingsGoalRepository.save(savingsGoal);
        }

        userService.subtractCash(addCashToGoalRequest.getAmount(), user.getId());
        transactionService.createTransactionForGoals(addCashToGoalRequest, user);

    }
}

package app.savingsGoal.service;

import app.exception.GoalNotFoundException;
import app.savingsGoal.model.SavingsGoal;
import app.savingsGoal.model.Status;
import app.savingsGoal.repository.SavingsGoalRepository;
import app.transaction.service.TransactionService;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.AddCashToGoalRequest;
import app.web.dto.CreateNewGoalRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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

        SavingsGoal goal = savingsGoalRepository.findById(addCashToGoalRequest.getGoalId()).orElseThrow(() -> new GoalNotFoundException("Goal not found"));

        if (goal.getStatus().equals(Status.COMPLETED)) {
            throw new GoalNotFoundException("Goal already completed");
        }
        goal.setCurrentAmount(goal.getCurrentAmount().add(addCashToGoalRequest.getAmount()));

        if (goal.getCurrentAmount().compareTo(goal.getTargetAmount()) >= 0) {
            goal.setStatus(Status.COMPLETED);
        }

        userService.subtractCash(addCashToGoalRequest.getAmount(), user.getId());
        transactionService.createTransactionForGoals(addCashToGoalRequest, user);
        savingsGoalRepository.save(goal);

    }

}

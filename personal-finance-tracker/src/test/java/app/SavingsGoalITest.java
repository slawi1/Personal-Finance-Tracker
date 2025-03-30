package app;

import app.savingsGoal.model.SavingsGoal;
import app.savingsGoal.service.SavingsGoalService;
import app.transaction.model.Transaction;
import app.transaction.repository.TransactionRepository;
import app.user.model.User;
import app.user.repository.UserRepository;
import app.user.service.UserService;
import app.web.dto.AddCashToGoalRequest;
import app.web.dto.CreateNewGoalRequest;
import app.web.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class SavingsGoalITest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SavingsGoalService savingsGoalService;
    @Autowired
    private TransactionRepository transactionRepository;


    @Test
    void testCreatingSavingsGoal() {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("user123")
                .email("user123@gmail.com")
                .password("1234")
                .confirmPassword("1234")
                .build();

        userService.register(registerRequest);
        User user = userRepository.findByUsername("user123").get();

        CreateNewGoalRequest request = CreateNewGoalRequest.builder()
                .goalName("testGoal")
                .targetAmount(BigDecimal.TEN)
                .deadline(LocalDate.now())
                .build();

        savingsGoalService.createNewGoal(request, user);
        user = userRepository.findByUsername("user123").get();
        List<SavingsGoal> goals = user.getGoals();
        SavingsGoal goal = goals.getFirst();
        assertEquals("testGoal", goal.getGoalName());
        assertThat(new BigDecimal(10), comparesEqualTo(goal.getTargetAmount()));
        assertThat(new BigDecimal(0), comparesEqualTo(goal.getCurrentAmount()));


    }


    @Test
    void testAddingCashToGoal() {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("user123")
                .email("user123@gmail.com")
                .password("1234")
                .confirmPassword("1234")
                .build();

        userService.register(registerRequest);
        User user = userRepository.findByUsername("user123").get();

        CreateNewGoalRequest request = CreateNewGoalRequest.builder()
                .goalName("testGoal2")
                .targetAmount(BigDecimal.TEN)
                .deadline(LocalDate.now())
                .build();

        savingsGoalService.createNewGoal(request, user);
        user = userRepository.findByUsername("user123").get();
        List<SavingsGoal> goals = user.getGoals();
        SavingsGoal goal = goals.getFirst();

        AddCashToGoalRequest addCashRequest = new AddCashToGoalRequest();
        addCashRequest.setGoalId(goal.getId());
        addCashRequest.setAmount(BigDecimal.valueOf(1));

        savingsGoalService.addCashToGoal(addCashRequest, user);
        user = userRepository.findByUsername("user123").get();

        goals = user.getGoals();
        goal = goals.getFirst();
        Optional<Transaction> transaction = transactionRepository.findByOwner(user);
        Transaction transaction1 = transaction.get();
        assertEquals("testGoal2", goal.getGoalName());
        assertEquals(user, goal.getGoalOwner());
        assertThat(new BigDecimal(10), comparesEqualTo(goal.getTargetAmount()));
        assertThat(new BigDecimal(1), comparesEqualTo(goal.getCurrentAmount()));
        assertThat(new BigDecimal(-1), comparesEqualTo(user.getBalance()));


    }
}

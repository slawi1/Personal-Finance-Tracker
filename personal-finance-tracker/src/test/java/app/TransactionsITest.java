package app;

import app.category.model.Category;
import app.category.repository.CategoryRepository;
import app.transaction.model.Transaction;
import app.transaction.repository.TransactionRepository;
import app.transaction.service.TransactionService;
import app.user.model.User;
import app.user.repository.UserRepository;
import app.user.service.UserService;
import app.web.dto.AddCashRequest;
import app.web.dto.AddExpenseRequest;
import app.web.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class TransactionsITest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;


    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testCreateExpenseTransaction () {

        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("user123")
                .email("user123@gmail.com")
                .password("1234")
                .confirmPassword("1234")
                .build();

        userService.register(registerRequest);
        User user = userRepository.findByUsername("user123").get();
        Category randomCategory = user.getCategories().getFirst();

        AddExpenseRequest request = AddExpenseRequest.builder()
                .transactionName("test")
                .amount(BigDecimal.ONE)
                .category(UUID.randomUUID())
                .transactionDate(LocalDate.now())
                .description("test")
                .build();

        Transaction transaction = transactionService.createExpenseTransaction(request, user, randomCategory.getId());

        User user1 = userRepository.findByUsername("user123").get();
        Optional<Transaction> optionalTransaction = transactionRepository.findByOwner(user);
        assertTrue(optionalTransaction.isPresent());
        assertThat(new BigDecimal("1.00"), comparesEqualTo(optionalTransaction.get().getAmount()));
        assertThat(new BigDecimal("-1.00"), comparesEqualTo(user1.getBalance()));


    }

    @Test
    void testAddCashTransaction() {

        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("user123")
                .email("user123@gmail.com")
                .password("1234")
                .confirmPassword("1234")
                .build();

        userService.register(registerRequest);

        User user = userRepository.findByUsername("user123").get();
        Optional<Category> incomeCategory = categoryRepository.findCategoryByNameAndOwner("Income", user);



        AddCashRequest request = AddCashRequest.builder()
                .sourceOfIncome("test")
                .description("testDescription")
                .date(LocalDate.now())
                .category(incomeCategory.get().getId())
                .amount(BigDecimal.ONE)
                .build();

        transactionService.addCashTransaction(request, user, incomeCategory.get().getId());

        User user1 = userRepository.findByUsername("user123").get();
        Optional<Transaction> optionalTransaction = transactionRepository.findByOwner(user);
        assertTrue(optionalTransaction.isPresent());
        assertThat(new BigDecimal("1.00"), comparesEqualTo(optionalTransaction.get().getAmount()));
        assertThat(new BigDecimal("1.00"), comparesEqualTo(user1.getBalance()));
    }


}

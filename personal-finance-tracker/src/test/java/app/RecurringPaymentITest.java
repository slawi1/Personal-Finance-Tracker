package app;

import app.category.model.Category;
import app.recurringPayment.model.Frequency;
import app.recurringPayment.model.RecurringPayment;
import app.recurringPayment.repository.RecurringPaymentRepository;
import app.recurringPayment.service.RecurringPaymentService;
import app.user.model.User;
import app.user.repository.UserRepository;
import app.user.service.UserService;
import app.web.dto.RecurringPaymentRequest;
import app.web.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class RecurringPaymentITest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    @Autowired
    private RecurringPaymentService recurringPaymentService;

    @Autowired
    private RecurringPaymentRepository recurringPaymentRepository;

    @Test
    void testCreateNewRecurringPayment() {

        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("user123")
                .email("user123@gmail.com")
                .password("1234")
                .confirmPassword("1234")
                .build();

        userService.register(registerRequest);
        User user = userRepository.findByUsername("user123").get();
        Category randomCategory = user.getCategories().getFirst();

        RecurringPaymentRequest request = RecurringPaymentRequest.builder()
                .paymentName("testName")
                .amount(BigDecimal.ONE)
                .paymentDate(LocalDate.now())
                .categoryId(randomCategory.getId())
                .frequency(Frequency.DAILY)
                .description("test")
                .build();

        recurringPaymentService.createRecurringPayment(request, user);

        List<RecurringPayment> payments = recurringPaymentRepository.findAll();
        assertEquals(1, payments.size());
        assertEquals("testName", payments.getFirst().getName());
        assertInstanceOf(RecurringPayment.class, payments.getFirst());


    }
}

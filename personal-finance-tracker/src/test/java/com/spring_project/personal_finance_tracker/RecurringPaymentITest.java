package com.spring_project.personal_finance_tracker;

import com.spring_project.category.model.Category;
import com.spring_project.recurringPayment.model.Frequency;
import com.spring_project.recurringPayment.model.RecurringPayment;
import com.spring_project.recurringPayment.repository.RecurringPaymentRepository;
import com.spring_project.recurringPayment.service.RecurringPaymentService;
import com.spring_project.user.model.User;
import com.spring_project.user.repository.UserRepository;
import com.spring_project.user.service.UserService;
import com.spring_project.web.dto.RecurringPaymentRequest;
import com.spring_project.web.dto.RegisterRequest;
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

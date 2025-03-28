package com.spring_project.personal_finance_tracker;

import com.spring_project.category.model.Category;
import com.spring_project.category.repository.CategoryRepository;
import com.spring_project.user.model.Role;
import com.spring_project.user.model.User;
import com.spring_project.user.repository.UserRepository;
import com.spring_project.user.service.UserService;
import com.spring_project.web.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class RegisterITest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void registerUser_happyPath() {

        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("user123")
                .email("user123@gmail.com")
                .password("1234")
                .confirmPassword("1234")
                .build();

        userService.register(registerRequest);

        Optional<User> optionalUser = userRepository.findByUsername(registerRequest.getUsername());
        assertTrue(userRepository.findByUsername(registerRequest.getUsername()).isPresent());
        assertEquals(1, userRepository.findAll().size());
        User user = optionalUser.get();


        List<Category> categories = categoryRepository.findAll();
        assertEquals(5, categories.size());
        assertEquals(0, user.getTransactions().size());
        assertEquals(0, user.getGoals().size());
        assertEquals(0, user.getRecurringPayment().size());
        assertEquals(Role.ADMIN, user.getRole());
    }
}

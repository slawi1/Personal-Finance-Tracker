package com.spring_project.personal_finance_tracker;

import com.spring_project.category.model.Category;
import com.spring_project.category.repository.CategoryRepository;
import com.spring_project.category.service.CategoryService;
import com.spring_project.exception.CategoryAlreadyExistsException;
import com.spring_project.exception.MaximumCategoriesReachedException;
import com.spring_project.user.model.User;
import com.spring_project.user.repository.UserRepository;
import com.spring_project.user.service.UserService;
import com.spring_project.web.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class AddCategoryITest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testAddingNewCategory() {

        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("user123")
                .email("user123@gmail.com")
                .password("1234")
                .confirmPassword("1234")
                .build();

        userService.register(registerRequest);
        User user = userRepository.findByUsername("user123").get();

        categoryService.addCategory("New Category", user);
        Optional<Category> optional = categoryRepository.findCategoryByNameAndOwner("New Category",user);
        Category category = optional.get();
        assertEquals(category.getOwner().getId(), user.getId());
        assertThat(new BigDecimal(0), comparesEqualTo(category.getAmount()));
        assertFalse(category.isSystemCategories());


    }

    @Test
    void testAddingMoreThanMaxAllowedCategory() {

        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("user123")
                .email("user123@gmail.com")
                .password("1234")
                .confirmPassword("1234")
                .build();

        userService.register(registerRequest);
        User user = userRepository.findByUsername("user123").get();

        categoryService.addCategory("New Category", user);
        categoryService.addCategory("New Category2", user);
        categoryService.addCategory("New Category3", user);
        categoryService.addCategory("New Category4", user);
        categoryService.addCategory("New Category5", user);
        categoryService.addCategory("New Category6", user);
        categoryService.addCategory("New Category7", user);
        categoryService.addCategory("New Category8", user);
        categoryService.addCategory("New Category9", user);
        categoryService.addCategory("New Category0", user);

        assertThrows(MaximumCategoriesReachedException.class , () -> categoryService.addCategory("New Category11", user));


    }
    @Test
    void testAddingTwoCategoriesWithSameName() {

        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("user123")
                .email("user123@gmail.com")
                .password("1234")
                .confirmPassword("1234")
                .build();

        userService.register(registerRequest);
        User user = userRepository.findByUsername("user123").get();

        categoryService.addCategory("New Category", user);


        assertThrows(CategoryAlreadyExistsException.class , () -> categoryService.addCategory("New Category", user));


    }
}

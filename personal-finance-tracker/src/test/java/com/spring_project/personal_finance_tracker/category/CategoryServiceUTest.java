package com.spring_project.personal_finance_tracker.category;

import com.spring_project.category.model.Category;
import com.spring_project.category.repository.CategoryRepository;
import com.spring_project.category.service.CategoryService;
import com.spring_project.exception.DomainException;
import com.spring_project.transaction.model.Transaction;
import com.spring_project.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceUTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void givenNonExistingCategoryId_whenFindCategoryById_thenThrowDomainException() {

        UUID categoryId = UUID.randomUUID();
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> categoryService.findCategoryById(categoryId));
    }


    @Test
    void givenValidCategory_whenDeleteCategory_thenSetIsDeletedTrue() {
        UUID id = UUID.randomUUID();
        Category category = new Category();
        when(categoryRepository.findCategoryById(id)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(id);

        assertTrue(category.isDeleted());
        verify(categoryRepository, times(1)).save(category);
    }
    @Test
    void givenNotValidCategory_whenDeleteCategory_thenNotSetIsDeletedTrue() {
        UUID id = UUID.randomUUID();
        Category category = new Category();
        when(categoryRepository.findCategoryById(id)).thenReturn(Optional.empty());

        categoryService.deleteCategory(id);

        assertFalse(category.isDeleted());
        verify(categoryRepository, times(0)).save(category);
    }

    @Test
    void givenCategoryWithZeroBalanceAndTransactionWithAmountOne_whenAddAmount_thenCategoryBalanceIsOne () {
        User user = new User();
        Category category = Category.builder()
                .amount(BigDecimal.ZERO)
                .name("name")
                .build();
        Transaction transaction = Transaction.builder()
                .amount(BigDecimal.ONE)
                .category(category)
                .build();

        when(categoryRepository.findCategoryByNameAndOwner(category.getName(), user)).thenReturn(Optional.of(category));

        categoryService.addAmount(transaction, user);

        assertEquals(BigDecimal.ONE, category.getAmount());
        verify(categoryRepository, times(1)).save(category);
    }

}

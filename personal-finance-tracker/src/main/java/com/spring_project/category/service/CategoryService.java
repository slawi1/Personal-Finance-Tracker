package com.spring_project.category.service;

import com.spring_project.category.model.Category;
import com.spring_project.category.repository.CategoryRepository;
import com.spring_project.exception.CategoryAlreadyExistsException;
import com.spring_project.exception.DomainException;
import com.spring_project.exception.MaximumCategoriesReachedException;
import com.spring_project.transaction.model.Transaction;
import com.spring_project.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;

    }

    public void addDefaultCategories(User user) {
        List<Category> categories = List.of(
                new Category("Income", user, BigDecimal.valueOf(0),true),
                new Category("Shopping", user, BigDecimal.valueOf(0), false),
                new Category("Food & Drinks", user, BigDecimal.valueOf(0), false),
                new Category("Bills & Utilities", user, BigDecimal.valueOf(0), false),
                new Category("Goals", user, BigDecimal.valueOf(0), true)
        );
        categoryRepository.saveAll(categories);
    }

    public void addCategory(String categoryName, User user, BigDecimal amount) {

        if (reachedMaximumCategories(user)) {
            throw new MaximumCategoriesReachedException("Maximum number of categories reached");
        }
        if (categoryRepository.findCategoryByNameAndOwner(categoryName, user).isPresent()) {
            throw new CategoryAlreadyExistsException("Category already exists");
        }
        Category newCategory = Category.builder()
                .name(categoryName)
                .owner(user)
                .amount(amount)
                .systemCategories(false)
                .build();
        categoryRepository.save(newCategory);
    }

    public Category findCategoryById(UUID id) {
        return categoryRepository.findById(id).orElseThrow(() -> new DomainException("Category not found"));
    }

    public Category findCategoryByNameAndOwner(String categoryName, User user) {

        return categoryRepository.findCategoryByNameAndOwner(categoryName, user).orElseThrow(() -> new DomainException("Category not found"));

    }

    public Boolean reachedMaximumCategories(User user) {
        List<Category> userCategories = categoryRepository.findAllByOwner(user);
        return userCategories.size() >= 15;
    }


    public void addAmount(Transaction transaction, User user) {

        Category category = findCategoryByNameAndOwner(transaction.getCategory().getName(), user);

        category.setAmount(category.getAmount().add(transaction.getAmount()));
        categoryRepository.save(category);

    }

}

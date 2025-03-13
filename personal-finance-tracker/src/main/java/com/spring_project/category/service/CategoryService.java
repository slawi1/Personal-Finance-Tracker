package com.spring_project.category.service;

import com.spring_project.category.model.Category;
import com.spring_project.category.repository.CategoryRepository;
import com.spring_project.user.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;

    }

    public void addDefaultCategories(User user) {
        List<Category> categories = List.of(
                new Category("Shopping", user),
                new Category("Food & Drinks", user),
                new Category("Bills & Utilities", user),
                new Category("Other", user)
        );
        categoryRepository.saveAll(categories);
    }

    public void addCategory(String categoryName, User user) {
        Category category = new Category(categoryName, user);
        categoryRepository.save(category);
    }
}

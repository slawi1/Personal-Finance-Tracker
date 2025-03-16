package com.spring_project.category.service;

import com.spring_project.category.model.Category;
import com.spring_project.category.repository.CategoryRepository;
import com.spring_project.exception.DomainException;
import com.spring_project.transaction.model.Transaction;
import com.spring_project.user.model.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;



    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;


    }

    public void addDefaultCategories(User user) {
        List<Category> categories = List.of(
                new Category("Prihodi", user, BigDecimal.valueOf(0)),
                new Category("Shopping", user, BigDecimal.valueOf(0)),
                new Category("Food & Drinks", user, BigDecimal.valueOf(0)),
                new Category("Bills & Utilities", user, BigDecimal.valueOf(0)),
                new Category("Other", user, BigDecimal.valueOf(0))
        );
        categoryRepository.saveAll(categories);
    }

    public void addCategory(String categoryName, User user, BigDecimal amount) {
        Category newCategory = new Category(categoryName, user, amount);
        categoryRepository.save(newCategory);
    }

    public  Category findCategoryById(UUID id) {
        return categoryRepository.findById(id).orElseThrow(()-> new DomainException("Category not found"));
    }

    public Category findCategoryByName(String name) {
        return categoryRepository.findCategoryByName(name).orElseThrow(()-> new DomainException("Category not found"));
    }

    public Category findCategoryByNameAndOwner(String categoryName, User user) {

        return categoryRepository.findCategoryByNameAndOwner(categoryName, user).orElseThrow(()-> new DomainException("Category not found"));

    }

    public void addAmount(Transaction transaction,User user) {


        Category category = findCategoryByNameAndOwner(transaction.getCategory().getName(), user);

        category.setAmount(category.getAmount().add(transaction.getAmount()));
        categoryRepository.save(category);

    }

    public void addCash(Transaction transaction) {
        Category category = findCategoryByName(transaction.getCategory().getName());
        category.setAmount(category.getAmount().add(transaction.getAmount()));
        categoryRepository.save(category);
    }

//    public void addCash(User user, AddCashRequest addCashRequest) {
//
//        Category category = findCategoryById(userService.getCategoryId(user));
//        category.setAmount(category.getAmount().add(addCashRequest.getAmount()));
//        categoryRepository.save(category);
//    }



}

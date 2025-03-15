package com.spring_project.transaction.service;

import com.spring_project.category.model.Category;
import com.spring_project.category.service.CategoryService;
import com.spring_project.transaction.model.Transaction;
import com.spring_project.transaction.model.Type;
import com.spring_project.transaction.repository.TransactionRepository;
import com.spring_project.user.model.User;
import com.spring_project.web.dto.AddExpenseRequest;
import com.spring_project.web.dto.AddTransactionRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryService categoryService;



    public TransactionService(TransactionRepository transactionRepository, CategoryService categoryService) {
        this.transactionRepository = transactionRepository;
        this.categoryService = categoryService;
    }


    public Transaction createExpenseTransaction(AddExpenseRequest addExpenseRequest, User user, UUID category) {

        Transaction transaction = Transaction.builder()
                .transactionName(addExpenseRequest.getTransactionName())
                .amount(addExpenseRequest.getAmount())
                .category(categoryService.findCategoryById(category))
                .owner(user)
                .type(Type.EXPENSE)
                .transactionDate(addExpenseRequest.getTransactionDate())
                .description("")
                .build();

        transactionRepository.save(transaction);
        categoryService.addAmount(transaction);
        return transaction;
    }

    public Transaction createTransaction(AddTransactionRequest addTransactionRequest, User user, UUID categoryId) {

        Transaction transaction = Transaction.builder()
                .transactionName(addTransactionRequest.getTransactionName())
                .amount(addTransactionRequest.getAmount())
                .category(categoryService.findCategoryById(categoryId))
                .owner(user)
                .type(addTransactionRequest.getTransactionType())
                .transactionDate(addTransactionRequest.getTransactionDate())
                .description(addTransactionRequest.getDescription())
                .build();

        return transactionRepository.save(transaction);
    }
}

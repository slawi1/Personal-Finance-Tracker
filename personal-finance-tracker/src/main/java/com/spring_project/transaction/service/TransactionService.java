package com.spring_project.transaction.service;

import com.spring_project.category.service.CategoryService;
import com.spring_project.transaction.model.Transaction;
import com.spring_project.transaction.model.Type;
import com.spring_project.transaction.repository.TransactionRepository;
import com.spring_project.user.model.User;
import com.spring_project.user.service.UserService;
import com.spring_project.web.dto.AddCashRequest;
import com.spring_project.web.dto.AddExpenseRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryService categoryService;
    private final UserService userService;



    public TransactionService(TransactionRepository transactionRepository, CategoryService categoryService, UserService userService) {
        this.transactionRepository = transactionRepository;
        this.categoryService = categoryService;
        this.userService = userService;
    }


    @Transactional
    public Transaction createExpenseTransaction(AddExpenseRequest addExpenseRequest, User user, UUID category) {

        Transaction transaction = Transaction.builder()
                .transactionName(addExpenseRequest.getTransactionName())
                .amount(addExpenseRequest.getAmount())
                .category(categoryService.findCategoryById(category))
                .owner(user)
                .type(Type.EXPENSE)
                .transactionDate(addExpenseRequest.getTransactionDate())
                .description(addExpenseRequest.getDescription())
                .build();

        transactionRepository.save(transaction);
        userService.subtractCash(addExpenseRequest.getAmount(), user.getId());
        categoryService.addAmount(transaction, user);
        return transaction;
    }

    @Transactional
    public Transaction addCashTransaction(AddCashRequest addCashRequest,User user, UUID category) {
        Transaction transaction = Transaction.builder()
                .transactionName(addCashRequest.getSourceOfIncome())
                .amount(addCashRequest.getAmount())
                .owner(user)
                .category(categoryService.findCategoryById(category))
                .type(Type.INCOME)
                .transactionDate(addCashRequest.getDate())
                .description(addCashRequest.getDescription())
                .build();

        transactionRepository.save(transaction);
        userService.addCash(addCashRequest, user.getId());
        categoryService.addAmount(transaction, user);
        return transaction;

    }

}

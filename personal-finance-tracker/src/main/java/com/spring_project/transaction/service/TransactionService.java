package com.spring_project.transaction.service;

import com.spring_project.category.service.CategoryService;
import com.spring_project.notification.service.NotificationService;
import com.spring_project.recurringPayment.model.RecurringPayment;
import com.spring_project.transaction.model.Transaction;
import com.spring_project.transaction.model.Type;
import com.spring_project.transaction.repository.TransactionRepository;
import com.spring_project.user.model.User;
import com.spring_project.user.service.UserService;
import com.spring_project.web.dto.AddCashRequest;
import com.spring_project.web.dto.AddCashToGoalRequest;
import com.spring_project.web.dto.AddExpenseRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;


@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryService categoryService;
    private final UserService userService;
    private final NotificationService notificationService;



    public TransactionService(TransactionRepository transactionRepository, CategoryService categoryService, UserService userService, NotificationService notificationService) {
        this.transactionRepository = transactionRepository;
        this.categoryService = categoryService;
        this.userService = userService;
        this.notificationService = notificationService;
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
    public void addCashTransaction(AddCashRequest addCashRequest,User user, UUID category) {
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

    }

    @Transactional
    public void createTransactionForGoals(AddCashToGoalRequest addCashToGoalRequest, User user) {

        Transaction transaction = Transaction.builder()
                .transactionName("To goals")
                .amount(addCashToGoalRequest.getAmount())
                .owner(user)
                .category(categoryService.findCategoryByNameAndOwner("Goals", user))
                .type(Type.EXPENSE)
                .transactionDate(LocalDate.now())
                .description("Adding cash to goal")
                .build();

        transactionRepository.save(transaction);
        categoryService.addAmount(transaction, user);
    }


    @Transactional
    public void createTransactionForRecurringPayments(RecurringPayment payment) {
        Transaction transaction = Transaction.builder()
                .transactionName(payment.getName())
                .amount(payment.getAmount())
                .category(categoryService.findCategoryById(payment.getCategoryId()))
                .owner(payment.getOwner())
                .type(Type.EXPENSE)
                .transactionDate(payment.getPaymentDate())
                .description("Automatic payment for " + payment.getName())
                .build();

        transactionRepository.save(transaction);
        userService.subtractCash(payment.getAmount(), payment.getOwner().getId());
        categoryService.addAmount(transaction, payment.getOwner());

        String body = "You have been automatically charged $ " + payment.getAmount() + " for " +  payment.getName();
        notificationService.sendEmail(payment.getOwner().getId(), "Recurring payment", body);
    }

}

package app.transaction.service;

import app.category.service.CategoryService;
import app.notification.service.NotificationService;
import app.recurringPayment.model.RecurringPayment;
import app.transaction.model.Transaction;
import app.transaction.model.Type;
import app.transaction.repository.TransactionRepository;
import app.user.model.User;
import app.user.service.UserService;
import app.web.dto.AddCashRequest;
import app.web.dto.AddCashToGoalRequest;
import app.web.dto.AddExpenseRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
                .transactionCreationDate(LocalDateTime.now())
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
                .transactionCreationDate(LocalDateTime.now())
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
                .transactionCreationDate(LocalDateTime.now())
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
                .transactionCreationDate(LocalDateTime.now())
                .description("Automatic payment for " + payment.getName())
                .build();

        transactionRepository.save(transaction);
        userService.subtractCash(payment.getAmount(), payment.getOwner().getId());
        categoryService.addAmount(transaction, payment.getOwner());

        String body = "You have been automatically charged $ " + payment.getAmount() + " for " +  payment.getName();
        notificationService.sendEmail(payment.getOwner().getId(), "Recurring payment", body);
    }

}

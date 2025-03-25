package com.spring_project.schedulers;

import com.spring_project.recurringPayment.model.RecurringPayment;
import com.spring_project.recurringPayment.service.RecurringPaymentService;
import com.spring_project.transaction.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class TransactionForRecurringPaymentsScheduler {

    private final TransactionService transactionService;
    private final RecurringPaymentService recurringPaymentService;



    public TransactionForRecurringPaymentsScheduler(TransactionService transactionService, RecurringPaymentService recurringPaymentService) {
        this.transactionService = transactionService;
        this.recurringPaymentService = recurringPaymentService;

    }

//    @Scheduled(cron = "0,30 * * * * *")
//    public void createTransactionForRecurringPayment() {
//
//        List<RecurringPayment> payments = recurringPaymentService.getAllRecurringPaymentsReadyToPay();
//        if (payments.isEmpty()) {
//            log.info("No recurring payments found");
//
//        }
//        for (RecurringPayment payment : payments) {
//
//
//            transactionService.createTransactionForRecurringPayments(payment);
//            recurringPaymentService.updateNextPayDate(payment);
//        }
//
//    }

}

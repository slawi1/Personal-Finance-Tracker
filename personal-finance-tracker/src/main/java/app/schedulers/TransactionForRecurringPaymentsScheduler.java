package app.schedulers;

import app.recurringPayment.model.RecurringPayment;
import app.recurringPayment.service.RecurringPaymentService;
import app.transaction.service.TransactionService;
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

    @Scheduled(cron = "0 * * * * *")
    public void createTransactionForRecurringPayment() {

        List<RecurringPayment> payments = recurringPaymentService.getAllRecurringPaymentsReadyToPay();
        if (payments.isEmpty()) {
            log.info("No recurring payments found");

        }
        for (RecurringPayment payment : payments) {

            try {
                transactionService.createTransactionForRecurringPayments(payment);
                recurringPaymentService.updateNextPayDate(payment);
            }catch (Exception e) {
                log.error("");
            }

        }

    }

}

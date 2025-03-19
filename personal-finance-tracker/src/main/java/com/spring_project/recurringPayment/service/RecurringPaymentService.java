package com.spring_project.recurringPayment.service;

import com.spring_project.exception.DomainException;
import com.spring_project.recurringPayment.model.RecurringPayment;
import com.spring_project.recurringPayment.model.Status;
import com.spring_project.recurringPayment.repository.RecurringPaymentRepository;
import com.spring_project.user.model.User;
import com.spring_project.web.dto.RecurringPaymentRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RecurringPaymentService {

    private final RecurringPaymentRepository recurringPaymentRepository;


    public RecurringPaymentService(RecurringPaymentRepository recurringPaymentRepository) {
        this.recurringPaymentRepository = recurringPaymentRepository;
    }

    public void createRecurringPayment(RecurringPaymentRequest recurringPaymentRequest, User user) {

        RecurringPayment recurringPayment = RecurringPayment.builder()
                .name(recurringPaymentRequest.getPaymentName())
                .owner(user)
                .amount(recurringPaymentRequest.getAmount())
                .createDate(LocalDate.now())
                .paymentDate(recurringPaymentRequest.getPaymentDate())
                .frequency(recurringPaymentRequest.getFrequency())
                .categoryId(recurringPaymentRequest.getCategoryId())
                .description(recurringPaymentRequest.getDescription())
                .status(Status.ACTIVE)
                .build();
        recurringPaymentRepository.save(recurringPayment);

    }

    public List<RecurringPayment> getAllRecurringPaymentsReadyToPay() {

        return recurringPaymentRepository.findAllByStatusAndPaymentDateLessThanEqual(Status.ACTIVE, LocalDate.now());
    }


    public void updateNextPayDate(RecurringPayment recurringPayment) {
        switch (recurringPayment.getFrequency()) {
            case DAILY:
                recurringPayment.setPaymentDate(recurringPayment.getPaymentDate().plusDays(1));
                break;
            case MONTHLY:
                recurringPayment.setPaymentDate(recurringPayment.getPaymentDate().plusMonths(1));
                break;
            case WEEKLY:
                recurringPayment.setPaymentDate(recurringPayment.getPaymentDate().plusWeeks(1));
                break;
            case YEARLY:
                recurringPayment.setPaymentDate(recurringPayment.getPaymentDate().plusYears(1));
            default:
                throw new DomainException("Invalid frequency");
        }
        recurringPaymentRepository.save(recurringPayment);

    }


}

package com.spring_project.recurringPayment.service;

import com.spring_project.recurringPayment.repository.RecurringPaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class RecurringPaymentService {

    private final RecurringPaymentRepository recurringPaymentRepository;

    public RecurringPaymentService(RecurringPaymentRepository recurringPaymentRepository) {
        this.recurringPaymentRepository = recurringPaymentRepository;
    }




}

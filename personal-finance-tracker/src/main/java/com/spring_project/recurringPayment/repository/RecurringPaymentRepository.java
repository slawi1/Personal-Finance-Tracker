package com.spring_project.recurringPayment.repository;

import com.spring_project.recurringPayment.model.RecurringPayment;
import com.spring_project.recurringPayment.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface RecurringPaymentRepository extends JpaRepository<RecurringPayment, UUID> {


    List<RecurringPayment> findAllByStatusAndPaymentDateLessThanEqual(Status status, LocalDate paymentDateIsLessThan);


}

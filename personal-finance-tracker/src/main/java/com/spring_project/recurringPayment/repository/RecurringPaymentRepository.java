package com.spring_project.recurringPayment.repository;

import com.spring_project.recurringPayment.model.RecurringPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RecurringPaymentRepository extends JpaRepository<RecurringPayment, UUID> {
    List<RecurringPayment> findAllByCategoryId(UUID categoryId);
}

package app.recurringPayment.repository;

import app.recurringPayment.model.RecurringPayment;
import app.recurringPayment.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface RecurringPaymentRepository extends JpaRepository<RecurringPayment, UUID> {


    List<RecurringPayment> findAllByStatusAndPaymentDateLessThanEqual(Status status, LocalDate paymentDateIsLessThan);


}

package app.recurringPayment.service;

import app.exception.DomainException;
import app.recurringPayment.model.RecurringPayment;
import app.recurringPayment.model.Status;
import app.recurringPayment.repository.RecurringPaymentRepository;
import app.user.model.User;
import app.web.dto.RecurringPaymentRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public void changeStatus(UUID id) {
        Optional<RecurringPayment> recurringPayment = recurringPaymentRepository.findById(id);
        if (recurringPayment.isPresent()) {
            RecurringPayment payment = recurringPayment.get();
            if (payment.getStatus() == Status.ACTIVE) {
                payment.setStatus(Status.INACTIVE);
                recurringPaymentRepository.save(payment);
                return;
            }
            payment.setStatus(Status.ACTIVE);
            recurringPaymentRepository.save(payment);
        }
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

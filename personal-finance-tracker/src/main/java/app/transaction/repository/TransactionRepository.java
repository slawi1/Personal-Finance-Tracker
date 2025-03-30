package app.transaction.repository;

import app.transaction.model.Transaction;
import app.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    Optional<Transaction> findByOwner(User owner);

//    List<Transaction> findAllByCategoryId(UUID categoryId);
//
//
//    List<Transaction> findTransactionBy(UUID categoryId);
}

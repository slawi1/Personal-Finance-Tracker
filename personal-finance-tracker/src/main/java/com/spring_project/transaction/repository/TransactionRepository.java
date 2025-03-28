package com.spring_project.transaction.repository;

import com.spring_project.transaction.model.Transaction;
import com.spring_project.user.model.User;
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

package com.spring_project.transaction.repository;

import com.spring_project.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findAllByCategoryId(UUID categoryId);

    void removeTransactionByCategory_Id(UUID categoryId);
}

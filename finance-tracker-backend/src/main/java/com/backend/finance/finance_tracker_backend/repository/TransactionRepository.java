package com.backend.finance.finance_tracker_backend.repository;

import com.backend.finance.finance_tracker_backend.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCategory(String category);
}

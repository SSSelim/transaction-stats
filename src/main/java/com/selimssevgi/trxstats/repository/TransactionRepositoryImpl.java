package com.selimssevgi.trxstats.repository;

import com.selimssevgi.trxstats.domain.Transaction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Transaction repository implementation using in-memory data structure.
 */
@Service
public class TransactionRepositoryImpl implements TransactionRepository {

  private List<Transaction> transactions = new ArrayList<>();

  @Override
  public void save(Transaction transaction) {
    Objects.requireNonNull(transaction);
    transactions.add(transaction);
  }

  @Override
  public List<Transaction> findAll() {
    if (transactions.isEmpty()) {
      return Collections.emptyList();
    }

    return Collections.unmodifiableList(transactions);
  }
}

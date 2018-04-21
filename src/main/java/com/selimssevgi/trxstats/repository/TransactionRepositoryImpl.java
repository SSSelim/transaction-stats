package com.selimssevgi.trxstats.repository;

import com.selimssevgi.trxstats.domain.Transaction;
import com.selimssevgi.trxstats.domain.shared.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
  public List<Transaction> findAllBySpecification(Specification<Transaction> specification) {
      return transactions.stream()
              .filter(specification::isSatisfiedBy)
              .collect(Collectors.toList()); // returns empty list if none found
  }
}

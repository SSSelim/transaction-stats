package com.selimssevgi.trxstats.repository;

import com.selimssevgi.trxstats.domain.Transaction;
import com.selimssevgi.trxstats.domain.shared.Specification;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Transaction repository implementation using in-memory data structure.
 * The implementation is not thread-safe.
 */
@Service
public class TransactionRepositoryImpl implements TransactionRepository {

  /**
   * Keeping the newest transaction in the beginning of collection.
   * Helps to reach the one in the transaction limit faster.
   */
  private SortedSet<Transaction> transactions = new TreeSet<>(comparingByTime().reversed());

  private Comparator<Transaction> comparingByTime() {
    return Comparator.comparingLong(trx -> trx.time().value());
  }

  @Override
  public void save(Transaction transaction) {
    Objects.requireNonNull(transaction);
    transactions.add(transaction);
  }

  @Override
  public List<Transaction> findAllBySpecification(Specification<Transaction> specification) {
      return transactions.stream()
              // takeWhile would help use here. SortedSet would be more meaningful.
              .filter(specification::isSatisfiedBy)
              .collect(Collectors.toList()); // returns empty list if none found
  }
}

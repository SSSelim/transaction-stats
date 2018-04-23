package com.selimssevgi.trxstats.repository;

import com.selimssevgi.trxstats.domain.Transaction;
import com.selimssevgi.trxstats.storage.TransactionStorage;
import com.selimssevgi.trxstats.domain.specification.Specification;
import com.selimssevgi.trxstats.domain.shared.Statistics;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Primary
public class ImprovedTrxRepositoryImpl implements TransactionRepository {
  private final TransactionStorage storage;

  public ImprovedTrxRepositoryImpl(TransactionStorage storage) {
    this.storage = storage;
  }

  @Override
  public Statistics getStatistics() {
    return storage.getStatistics();
  }

  @Override
  public void save(Transaction transaction) {
    Objects.requireNonNull(transaction);
    storage.add(transaction);
  }

  @Override
  public List<Transaction> findAllBySpecification(Specification<Transaction> specification) {
    return Collections.emptyList(); //TODO:selimssevgi: should remove this method from interface
  }
}

package com.selimssevgi.trxstats.service;

import com.selimssevgi.trxstats.domain.Transaction;
import com.selimssevgi.trxstats.domain.shared.Statistics;
import com.selimssevgi.trxstats.domain.specification.TransactionSpecification;
import com.selimssevgi.trxstats.repository.TransactionRepository;
import com.selimssevgi.trxstats.service.model.NewTransactionRequest;
import com.selimssevgi.trxstats.service.model.TransactionStatisticsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Transaction service implementation.
 */
@Service
public class TransactionServiceImpl implements TransactionService {
  private static final Logger LOGGER = LoggerFactory.getLogger(TransactionServiceImpl.class);

  private final TransactionRepository transactionRepository;
  private final TransactionSpecification transactionSpecification;

  public TransactionServiceImpl(TransactionRepository transactionRepository,
                                TransactionSpecification transactionSpecification) {
    this.transactionRepository = transactionRepository;
    this.transactionSpecification = transactionSpecification;
  }

  @Override
  public void add(NewTransactionRequest newTransactionRequest) {
    Transaction transaction = TransactionServiceMapper.toTransaction(newTransactionRequest);

    if (transactionSpecification.isNotSatisfiedBy(transaction)) {
      LOGGER.info("Given transaction does not satisfy business rules: {}", transaction);
      throw new OldTransactionException("Transaction is old");
    }

    transactionRepository.save(transaction);
  }

  @Override
  public TransactionStatisticsDto calculateStatistics() {
    Statistics statistics = transactionRepository.getStatistics();
    return TransactionServiceMapper.toTransactionStaticsDto(statistics);
  }
}

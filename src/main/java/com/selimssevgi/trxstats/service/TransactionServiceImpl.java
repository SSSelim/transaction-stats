package com.selimssevgi.trxstats.service;

import com.selimssevgi.trxstats.domain.shared.Amount;
import com.selimssevgi.trxstats.domain.Transaction;
import com.selimssevgi.trxstats.repository.TransactionRepository;
import com.selimssevgi.trxstats.service.model.NewTransactionRequest;
import com.selimssevgi.trxstats.service.model.TransactionStatisticsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.DoubleSummaryStatistics;
import java.util.List;

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
      throw new OldTransactionException("Transaction is old");
    }

    transactionRepository.save(transaction);
  }

  @Override
  public TransactionStatisticsDto calculateStatistics() {
    List<Transaction> transactions =
            transactionRepository.findAllBySpecification(transactionSpecification);

    // DSStatistics returns non-zero values when no value found
    if (transactions.isEmpty()) {
      LOGGER.debug("No transaction found, returns zero valued statistics.");
      return TransactionStatisticsDto.fromZeroValues();
    }

    DoubleSummaryStatistics doubleSummaryStatistics = transactions.stream()
            .map(Transaction::amount)
            .mapToDouble(Amount::value)
            .collect(DoubleSummaryStatistics::new,
                    DoubleSummaryStatistics::accept,
                    DoubleSummaryStatistics::combine);

    return TransactionStatisticsDto.newBuilder()
            .sum(doubleSummaryStatistics.getSum())
            .maximum(doubleSummaryStatistics.getMax())
            .minimum(doubleSummaryStatistics.getMin())
            .average(doubleSummaryStatistics.getAverage())
            .count(doubleSummaryStatistics.getCount())
            .build();
  }
}

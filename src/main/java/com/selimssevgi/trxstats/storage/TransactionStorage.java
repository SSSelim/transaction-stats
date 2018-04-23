package com.selimssevgi.trxstats.storage;

import com.selimssevgi.trxstats.domain.Transaction;
import com.selimssevgi.trxstats.domain.specification.Specification;
import com.selimssevgi.trxstats.domain.shared.Statistics;
import com.selimssevgi.trxstats.domain.specification.TransactionSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Transaction storage.
 * <p>
 *   Adds given transactions to the underlying collection.
 *   Calculates statistical data as the transactions are accepted.
 *   Keeping cycling the collection to detects transactions
 *   that does not satisfies {@link TransactionSpecification}.
 *   When a new unacceptable transaction is detected,
 *   it excludes from the statistical data.
 * </p>
 */
@Component
public class TransactionStorage implements Storage<Transaction> {
  private static final  Logger LOGGER = LoggerFactory.getLogger(TransactionStorage.class);

  private final BlockingDeque<Transaction> transactions;

  private final StatisticsCalculator statisticsCalculator;

  private final Specification<Transaction> transactionSpecification;

  private static final int EXCLUDER_THREAD_COUNT = 3;

  private final ExecutorService pool =
          Executors.newFixedThreadPool(EXCLUDER_THREAD_COUNT);

  public TransactionStorage(BlockingDeque<Transaction> transactions,
                            Specification<Transaction> transactionSpecification,
                            StatisticsCalculator statisticsCalculator) {
    this.transactions = transactions;
    this.transactionSpecification = transactionSpecification;
    this.statisticsCalculator = statisticsCalculator;
    initExcluders();
  }

  /**
   * Initializes and submits excluder tasks.
   */
  private void initExcluders() {
    LOGGER.info("initializing excluder tasks");

    Runnable excluder = () -> {
      try {
        while (true) {
          if (Thread.currentThread().isInterrupted()) {
            return;
          }
          Transaction trx = transactions.take();

          if (transactionSpecification.isSatisfiedBy(trx)) {
            transactions.add(trx);
          } else {
            statisticsCalculator.exclude(trx.amount());
          }
        }
      } catch (InterruptedException e) {
        LOGGER.error("Interrupted");
        Thread.currentThread().interrupt();
      } catch (Exception ex) {
        LOGGER.error("Something wrong happened: {}", ex);
      }
    };

    for (int i = 0; i < 3; i++) {
      pool.submit(excluder);
    }
  }

  @Override
  public void add(Transaction transaction) {
    transactions.addLast(transaction);
    statisticsCalculator.include(transaction.amount());
  }

  @Override
  public Statistics getStatistics() {
    return statisticsCalculator.current();
  }
}

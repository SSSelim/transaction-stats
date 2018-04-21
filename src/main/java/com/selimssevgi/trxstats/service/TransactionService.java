package com.selimssevgi.trxstats.service;

import com.selimssevgi.trxstats.service.model.NewTransactionRequest;
import com.selimssevgi.trxstats.service.model.TransactionStatisticsDto;

/**
 * Provides service for new transaction creation, and transaction statistics.
 */
public interface TransactionService {

  /**
   * Adds a new transaction.
   *
   * @param newTransactionRequest carries data for new transaction
   * @throws NullPointerException if given argument is null
   * @throws OldTransactionException if transaction request is older than 60 seconds
   */
  void add(NewTransactionRequest newTransactionRequest);

  /**
   * Calculates the statistics of the transaction in last 60 seconds.
   *
   * @return object holding statistics data,
   *         if no transaction found, object is populated with zero-values
   */
  TransactionStatisticsDto calculateStatistics();
}

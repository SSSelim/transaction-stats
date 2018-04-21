package com.selimssevgi.trxstats.service;

import com.selimssevgi.trxstats.domain.Transaction;
import com.selimssevgi.trxstats.service.model.NewTransactionRequest;

import java.util.Objects;

/**
 * Maps service layer objects to domain layer objects and vice versa.
 */
class TransactionServiceMapper {

  private TransactionServiceMapper() {
    throw new AssertionError("No TransactionServiceMapper object for you!");
  }

  /**
   * Maps a {@link NewTransactionRequest} to {@link Transaction}.
   *
   * @param request data source to be transferred
   * @return valid transaction object
   * @throws NullPointerException if given input is null
   */
  static Transaction toTransaction(NewTransactionRequest request) {
    Objects.requireNonNull(request);
    return Transaction.of(request.getAmount(), request.getEpochTime());
  }
}

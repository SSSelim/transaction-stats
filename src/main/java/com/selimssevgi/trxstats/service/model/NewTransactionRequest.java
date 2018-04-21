package com.selimssevgi.trxstats.service.model;

import com.selimssevgi.trxstats.domain.shared.Amount;
import com.selimssevgi.trxstats.domain.shared.EpochTime;

/**
 * Request object for creating a new transaction.
 */
public class NewTransactionRequest {
  private final Amount amount;
  private final EpochTime epochTime;

  public NewTransactionRequest(Amount amount, EpochTime epochTime) {
    this.amount = amount;
    this.epochTime = epochTime;
  }

  public Amount getAmount() {
    return amount;
  }

  public EpochTime getEpochTime() {
    return epochTime;
  }
}

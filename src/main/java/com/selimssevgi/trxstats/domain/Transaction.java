package com.selimssevgi.trxstats.domain;

import com.selimssevgi.trxstats.domain.shared.Amount;
import com.selimssevgi.trxstats.domain.shared.EpochTime;

import java.util.Objects;

/**
 * Immutable transaction class.
 */
public class Transaction {
  private final Amount amount;
  private final EpochTime epochTime;

  private Transaction(Amount amount, EpochTime epochTime) {
    this.amount = amount;
    this.epochTime = epochTime;
  }

  public static Transaction of(Amount amount, EpochTime epochTime) {
    Objects.requireNonNull(amount);
    Objects.requireNonNull(epochTime);
    return new Transaction(amount, epochTime);
  }

  public Amount amount() {
    return amount; // it is an immutable object
  }

  public EpochTime time() {
    return epochTime; // it is an immutable object
  }

  public boolean isOlderThan(long seconds) {
    return epochTime.isBefore(EpochTime.fromSecondsAgo(seconds));
  }

  @Override
  public String toString() {
    return "Transaction{amount=" + amount +
            ", epochTime=" + epochTime +
            '}';
  }
}

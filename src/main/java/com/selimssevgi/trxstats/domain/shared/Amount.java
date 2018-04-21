package com.selimssevgi.trxstats.domain.shared;

import com.selimssevgi.trxstats.util.Requires;

import java.util.Objects;

/**
 * Immutable Amount value class.
 */
public final class Amount {
  private final double value;

  private Amount(double value) {
    this.value = value;
  }

  public static Amount of(double amount) {
    Requires.requireNonZero(amount);
    Requires.requireNonNegative(amount);
    return new Amount(amount);
  }

  public double value() {
    return value;
  }

  @Override
  public boolean equals(Object that) {
    if (this == that) {
      return true;
    }

    if (that == null || getClass() != that.getClass()) {
      return false;
    }
    Amount otherAmount = (Amount) that;
    return Double.compare(otherAmount.value(), this.value()) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return "Amount{value=" + value + '}';
  }
}

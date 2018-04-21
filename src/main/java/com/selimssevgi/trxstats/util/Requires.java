package com.selimssevgi.trxstats.util;

/**
 * Utility methods for quick checking of preconditions.
 */
public final class Requires {

  private Requires() {
    throw new AssertionError("No Requires object for you!");
  }

  public static void requireNonZero(double value) {
    if (value == 0.0) {
      throw new IllegalArgumentException("Value cannot be zero:" + value);
    }
  }

  public static void requireNonNegative(double value) {
    if (value < 0) {
      throw new IllegalArgumentException("Value cannot be negative value:" + value);
    }
  }

  public static void requireNonNegative(long value) {
    if (value < 0) {
      throw new IllegalArgumentException("Value cannot be negative value:" + value);
    }
  }
}

package com.selimssevgi.trxstats.domain.shared;

import com.selimssevgi.trxstats.util.Requires;

import java.time.Instant;
import java.util.Objects;

/**
 * Immutable EpochTime value class.
 */
public final class EpochTime implements Comparable<EpochTime> {
  private final long value;

  private EpochTime(long value) {
    this.value = value;
  }

  public static EpochTime of(long timeInMs) {
    Requires.requireNonNegative(timeInMs);
    return new EpochTime(timeInMs);
  }

  public static EpochTime fromSecondsAgo(long seconds) {
    return from(Instant.now().minusSeconds(seconds));
  }

  public static EpochTime from(Instant instant) {
    Objects.requireNonNull(instant);
    return of(instant.toEpochMilli());
  }

  public long value() {
    return value;
  }

  public boolean isBefore(EpochTime otherEpochTime) {
    return compareTo(otherEpochTime) < 0;
  }

  @Override
  public boolean equals(Object that) {
    if (this == that) {
      return true;
    }

    if (that == null || getClass() != that.getClass()) {
      return false;
    }

    EpochTime epochTime = (EpochTime) that;
    return this.value() == epochTime.value();
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return "EpochTime{value=" + value +
            ", humanReadable=" + Instant.ofEpochMilli(value) +
            '}';
  }

  /**
   * Compares this epochTime to the specified epochTime.
   * <p>
   * The comparison is based on the time-line position of the epochTime.
   *
   * @param otherEpochTime  the other epochTime to compare to, not null
   * @return the comparator value, negative if less, positive if greater
   * @throws NullPointerException if otherEpochTime is null
   */
  @Override
  public int compareTo(EpochTime otherEpochTime) {
    return Long.compare(value(), otherEpochTime.value());
  }
}

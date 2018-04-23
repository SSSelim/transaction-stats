package com.selimssevgi.trxstats.storage;

import com.selimssevgi.trxstats.domain.shared.Amount;
import com.selimssevgi.trxstats.domain.shared.Statistics;

/**
 * Statistics Calculator operations.
 */
public interface StatisticsCalculator {

  /**
   * Includes given amount to statistical calculation.
   *
   * @param amount value to be included
   */
  void include(Amount amount); // could be a parameterized type

  /**
   * Excludes given amount from statistical calculation.
   *
   * @param amount value to be excluded
   */
  void exclude(Amount amount);

  /**
   * Returns statistical data for the moment of request.
   *
   * @return statistical data object
   */
  Statistics current();
}

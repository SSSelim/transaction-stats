package com.selimssevgi.trxstats.storage;

import com.selimssevgi.trxstats.domain.shared.Statistics;

/**
 *
 * @param <T> the type of elements held in this storage
 */
public interface Storage<T> {

  /**
   * Adds given object to the underlying data store.
   *
   * @param t object to be added
   * @throws NullPointerException if given object is null
   */
  void add(T t);

  /**
   * Returns statistical data for the existing objects.
   *
   * @return statistical data
   */
  Statistics getStatistics();

}

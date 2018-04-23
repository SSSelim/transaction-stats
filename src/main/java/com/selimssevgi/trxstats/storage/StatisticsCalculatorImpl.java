package com.selimssevgi.trxstats.storage;

import com.selimssevgi.trxstats.domain.shared.Amount;
import com.selimssevgi.trxstats.domain.shared.Statistics;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Thread-safe statistics calculator.
 * <p>
 *   It is in continuous calculation mode.
 *   Items can included in statistic or excluded from.
 *   When requested, it returns statistical data at that time.
 *   Therefore, it does not calculate numbers when requested,
 *   its response time O(1).
 */
@Component
public class StatisticsCalculatorImpl implements StatisticsCalculator {

  private final Collection<Double> minimums =
          Collections.synchronizedCollection(new PriorityQueue<>());

  // reverse comparison for maximum element to be first.
  private final Collection<Double> maximums =
          Collections.synchronizedCollection(
                  new PriorityQueue<>((o1, o2) -> -1 * Double.compare(o1, o2)));

  ReentrantLock lock = new ReentrantLock();

  private DoubleAdder sum = new DoubleAdder();
  private AtomicLong count = new AtomicLong();

  /**
   * Includes given amount into statistical data.
   *
   * @param amount to be included
   */
  @Override
  public void include(Amount amount) {
    // lock.lock();
    sum.add(amount.value());
    count.incrementAndGet();
    minimums.add(amount.value());
    maximums.add(amount.value());
    // lock.unlock();
  }

  /**
   * Excludes given amount from statistical data.
   *
   * @param amount to be excluded
   */
  @Override
  public void exclude(Amount amount) {
    // lock.lock();
    sum.add(-1 * amount.value());
    count.decrementAndGet();
    minimums.remove(amount.value());
    maximums.remove(amount.value());
    // lock.unlock();
  }

  /**
   * Returns statistical data at the request time.
   *
   * @return statistics object
   */
  @Override
  public Statistics current() {
    // lock.lock();
    double sumCopy = sum.doubleValue();
    long countCopy = count.longValue();

    double avg = sumCopy == 0 ? 0.0 : sumCopy / countCopy;

    double min = 0.0;
    // have to synchronize around iterator, not thread-safe
    synchronized (minimums) {
      Iterator<Double> iterator = minimums.iterator();
      if (iterator.hasNext()) {
        min = iterator.next();
      }
    }

    double max = 0.0;
    synchronized (maximums) {
      Iterator<Double> iterator = maximums.iterator();
      if (iterator.hasNext()) {
        max = iterator.next();
      }
    }
    // lock.unlock();

    return Statistics.newBuilder()
            .sum(sumCopy)
            .average(avg)
            .maximum(max)
            .minimum(min)
            .count(countCopy)
            .build();
  }
}

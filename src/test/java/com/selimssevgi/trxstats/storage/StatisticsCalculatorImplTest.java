package com.selimssevgi.trxstats.storage;

import com.selimssevgi.trxstats.domain.shared.Amount;
import com.selimssevgi.trxstats.domain.shared.Statistics;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.DoubleSummaryStatistics;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.DoubleStream;

@RunWith(JUnit4.class)
public class StatisticsCalculatorImplTest {

  @Test
  public void shouldReturnZeroValuesWhenNoAmountAdded() {
    StatisticsCalculatorImpl emptyStats = new StatisticsCalculatorImpl();
    Statistics stats = emptyStats.current();

    Assertions.assertThat(stats.getMin()).isEqualTo(0.0);
    Assertions.assertThat(stats.getMax()).isEqualTo(0.0);
    Assertions.assertThat(stats.getSum()).isEqualTo(0.0);
    Assertions.assertThat(stats.getAvg()).isEqualTo(0.0);
    Assertions.assertThat(stats.getCount()).isEqualTo(0);
  }

  @Test
  public void shouldReturnZeroValuesWhenEmptied() {
    StatisticsCalculatorImpl emptyStats = new StatisticsCalculatorImpl();
    emptyStats.include(Amount.of(5.0));
    emptyStats.include(Amount.of(6.0));

    emptyStats.exclude(Amount.of(5.0));
    emptyStats.exclude(Amount.of(6.0));

    Statistics stats = emptyStats.current();

    Assertions.assertThat(stats.getMin()).isEqualTo(0.0);
    Assertions.assertThat(stats.getMax()).isEqualTo(0.0);
    Assertions.assertThat(stats.getSum()).isEqualTo(0.0);
    Assertions.assertThat(stats.getAvg()).isEqualTo(0.0);
    Assertions.assertThat(stats.getCount()).isEqualTo(0);
  }

  @Test
  public void shouldGetPreviousMin() {
    StatisticsCalculatorImpl stats = new StatisticsCalculatorImpl();

    stats.include(Amount.of(5.0));
    stats.include(Amount.of(6.0));
    stats.include(Amount.of(3.0));

    stats.exclude(Amount.of(3.0));

    Statistics finalStats = stats.current();

    Assertions.assertThat(finalStats.getMin()).isEqualTo(5.0);
  }

  @Test
  public void shouldPreserveDuplicates() {
    StatisticsCalculatorImpl stats = new StatisticsCalculatorImpl();

    stats.include(Amount.of(5.0));
    stats.include(Amount.of(5.0));
    stats.include(Amount.of(7.0));
    stats.include(Amount.of(7.0));

    stats.exclude(Amount.of(5.0));
    stats.include(Amount.of(7.0));

    Statistics finalStats = stats.current();

    Assertions.assertThat(finalStats.getMin()).isEqualTo(5.0);
    Assertions.assertThat(finalStats.getMax()).isEqualTo(7.0);
  }

  @Test
  public void shouldBeThreadSafe() throws InterruptedException {
    StatisticsCalculatorImpl statisticsCalculator = new StatisticsCalculatorImpl();

    final CountDownLatch latch = new CountDownLatch(1);
    ExecutorService pool = Executors.newFixedThreadPool(5);

    final int upperLimit = 500;

    for (int i = 1; i <= upperLimit; i++) {  // zero is not valid value for amount
      final int val = i;
      Runnable includer = () -> {
        try {
          latch.await();
          statisticsCalculator.include(Amount.of(val));
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      };

      pool.submit(includer);
    }

    // have them start at the same time
    latch.countDown();

    pool.shutdown();

    while (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
      // executor service is not done yet, wait more
    }

    DoubleSummaryStatistics dss =
            DoubleStream.iterate(1.0, x -> x + 1)
                    .limit(upperLimit)
                    .summaryStatistics();

    Statistics stats = statisticsCalculator.current();
    Assertions.assertThat(stats.getMin()).isEqualTo(dss.getMin());
    Assertions.assertThat(stats.getMax()).isEqualTo(dss.getMax());
    Assertions.assertThat(stats.getSum()).isEqualTo(dss.getSum());
    Assertions.assertThat(stats.getAvg()).isEqualTo(dss.getAverage());
    Assertions.assertThat(stats.getCount()).isEqualTo(dss.getCount());
  }

}
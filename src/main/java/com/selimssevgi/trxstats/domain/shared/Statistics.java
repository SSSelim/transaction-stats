package com.selimssevgi.trxstats.domain.shared;


/**
 * Immutable statistics class.
 * <p>
 *   Provides builder object for more convenient and readable way
 *   of creating the objects.
 *   All fields have their own zero values as defaults.
 */
public class Statistics {
  private double sum;
  private double avg;
  private double max;
  private double min;
  private long count;

  private Statistics(StatisticsBuilder builder) {
    this.sum = builder.sum;
    this.avg = builder.avg;
    this.max = builder.max;
    this.min = builder.min;
    this.count = builder.count;
  }

  // only builder instance could be used
  // on condition resetting values before returning instance
  // that will not be thread safe
  public static StatisticsBuilder newBuilder() {
    return new StatisticsBuilder();
  }

  public double getSum() {
    return sum;
  }

  public double getAvg() {
    return avg;
  }

  public double getMax() {
    return max;
  }

  public double getMin() {
    return min;
  }

  public long getCount() {
    return count;
  }

  @Override
  public String toString() {
    return "Statistics{" + "sum=" + sum +
            ", avg=" + avg +
            ", max=" + max +
            ", min=" + min +
            ", count=" + count +
            '}';
  }

  public static class StatisticsBuilder {
    private double sum = 0.0;
    private double avg = 0.0;
    private double max = 0.0;
    private double min = 0.0;
    private long count = 0L;

    //TODO:selimssevgi: could add validation for acceptable values, or replaces with defaults
    public StatisticsBuilder sum(double sum) {
      this.sum = sum;
      return this;
    }

    public StatisticsBuilder average(double avg) {
      this.avg = avg;
      return this;
    }

    public StatisticsBuilder maximum(double max) {
      this.max = max;
      return this;
    }

    public StatisticsBuilder minimum(double min) {
      this.min = min;
      return this;
    }

    public StatisticsBuilder count(long count) {
      this.count = count;
      return this;
    }

    public Statistics build() {
      return new Statistics(this);
    }
  }
}

package com.selimssevgi.trxstats.service.model;

/**
 * Container class for statistical data.
 * <p>
 *   Provides builder object for more convenient and readable way
 *   of creating the objects.
 *   All fields have their own zero values as defaults.
 *
 * TODO:selimssevgi: now this class is almost copy of Statistics, could remove builder,
 * then has to provide setter because there are not in same package.
 * Could use shared object. It may not seem necessary
 * but better to have a different response object for service.
 * Leaving it as it is for now.
 */
public class TransactionStatisticsDto {
  private double sum;
  private double avg;
  private double max;
  private double min;
  private long count;

  private TransactionStatisticsDto(TransactionStatisticsDtoBuilder builder) {
    this.sum = builder.sum;
    this.avg = builder.avg;
    this.max = builder.max;
    this.min = builder.min;
    this.count = builder.count;
  }

  public static TransactionStatisticsDtoBuilder newBuilder() {
    return new TransactionStatisticsDtoBuilder();
  }

  public static TransactionStatisticsDto fromZeroValues() {
    return newBuilder().build();
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

  public static class TransactionStatisticsDtoBuilder {
    private double sum = 0.0;
    private double avg = 0.0;
    private double max = 0.0;
    private double min = 0.0;
    private long count = 0L;

    //TODO:selimssevgi: could add validation for acceptable values, or replaces with defaults
    public TransactionStatisticsDtoBuilder sum(double sum) {
      this.sum = sum;
      return this;
    }

    public TransactionStatisticsDtoBuilder average(double avg) {
      this.avg = avg;
      return this;
    }

    public TransactionStatisticsDtoBuilder maximum(double max) {
      this.max = max;
      return this;
    }

    public TransactionStatisticsDtoBuilder minimum(double min) {
      this.min = min;
      return this;
    }

    public TransactionStatisticsDtoBuilder count(long count) {
      this.count = count;
      return this;
    }

    public TransactionStatisticsDto build() {
      return new TransactionStatisticsDto(this);
    }
  }
}

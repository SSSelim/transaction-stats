package com.selimssevgi.trxstats.service;

import com.selimssevgi.trxstats.domain.Transaction;
import com.selimssevgi.trxstats.domain.shared.Statistics;
import com.selimssevgi.trxstats.service.model.NewTransactionRequest;
import com.selimssevgi.trxstats.service.model.TransactionStatisticsDto;

import java.util.Objects;

/**
 * Maps service layer objects to domain layer objects and vice versa.
 */
final class TransactionServiceMapper {

  private TransactionServiceMapper() {
    throw new AssertionError("No TransactionServiceMapper object for you!");
  }

  /**
   * Maps a {@link NewTransactionRequest} to {@link Transaction}.
   *
   * @param request data source to be transferred
   * @return valid transaction object
   * @throws NullPointerException if given input is null
   */
  static Transaction toTransaction(NewTransactionRequest request) {
    Objects.requireNonNull(request);
    return Transaction.of(request.getAmount(), request.getEpochTime());
  }

  /**
   * Maps a {@link Statistics} to {@link TransactionStatisticsDto}.
   *
   * @param statistics data source to be transferred
   * @return valid dto object
   * @throws NullPointerException if given input is null
   */
  static TransactionStatisticsDto toTransactionStaticsDto(Statistics statistics) {
    Objects.requireNonNull(statistics);
    return TransactionStatisticsDto.newBuilder()
            .sum(statistics.getSum())
            .count(statistics.getCount())
            .average(statistics.getAvg())
            .maximum(statistics.getMax())
            .minimum(statistics.getMin())
            .build();
  }
}

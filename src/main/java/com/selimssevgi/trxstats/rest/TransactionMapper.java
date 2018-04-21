package com.selimssevgi.trxstats.rest;

import com.selimssevgi.trxstats.domain.shared.Amount;
import com.selimssevgi.trxstats.domain.shared.EpochTime;
import com.selimssevgi.trxstats.rest.model.TransactionInput;
import com.selimssevgi.trxstats.rest.model.TransactionStatisticsOutput;
import com.selimssevgi.trxstats.service.model.NewTransactionRequest;
import com.selimssevgi.trxstats.service.model.TransactionStatisticsDto;

import java.util.Objects;

/**
 * Maps web layer objects to service layer objects and vice versa.
 */
final class TransactionMapper {

  private TransactionMapper() {
    throw new AssertionError("No TransactionMapper object for you!");
  }

  /**
   * Maps a {@link TransactionStatisticsDto} to {@link TransactionStatisticsOutput}.
   *
   * @param dto data source to be transferred
   * @return output object
   * @throws NullPointerException if given dto is null
   */
  static TransactionStatisticsOutput toTransactionStatisticsOutput(TransactionStatisticsDto dto) {
    Objects.requireNonNull(dto);
    TransactionStatisticsOutput trxStatsOutput = new TransactionStatisticsOutput();
    trxStatsOutput.setSum(dto.getSum());
    trxStatsOutput.setMax(dto.getMax());
    trxStatsOutput.setMin(dto.getMin());
    trxStatsOutput.setAvg(dto.getAvg());
    trxStatsOutput.setCount(dto.getCount());
    return trxStatsOutput;
  }

  /**
   * Maps a {@link TransactionInput} to {@link NewTransactionRequest}.
   *
   * @param transactionInput data source to be transferred
   * @return valid request object
   * @throws NullPointerException if given input is null
   * @throws IllegalArgumentException if input values are not valid
   */
  static NewTransactionRequest toNewTransactionRequest(TransactionInput transactionInput) {
    Objects.requireNonNull(transactionInput);
    return new NewTransactionRequest(
            Amount.of(transactionInput.getAmount()),
            EpochTime.of(transactionInput.getTimestamp())
    );
  }
}

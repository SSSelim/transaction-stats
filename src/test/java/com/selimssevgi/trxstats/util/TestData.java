package com.selimssevgi.trxstats.util;

import com.selimssevgi.trxstats.domain.shared.Amount;
import com.selimssevgi.trxstats.domain.shared.EpochTime;
import com.selimssevgi.trxstats.service.TransactionSpecification;

public class TestData {
  public static final Amount VALID_AMOUNT = Amount.of(26.0);
  public static final EpochTime VALID_EPOCH_TIME = EpochTime.fromSecondsAgo(26);

  public static final EpochTime OLDER_THAN_ACCEPTED_TRX_TIME_LIMIT =
          EpochTime.fromSecondsAgo(TransactionSpecification.TRX_TIME_LIMIT_IN_SECONDS + 2);

  public static final EpochTime IN_ACCEPTED_TRX_TIME_LIMIT =
          EpochTime.fromSecondsAgo(TransactionSpecification.TRX_TIME_LIMIT_IN_SECONDS - 24);

  //TODO:selimssevgi: mathematically not correct
  public static final double SUM = 62.0;
  public static final double MAX = 26.0;
  public static final double MIN = 2.6;
  public static final double AVG = 6.2;
  public static final long COUNT = 62L;
}

package com.selimssevgi.trxstats.util;

import com.selimssevgi.trxstats.domain.shared.Amount;
import com.selimssevgi.trxstats.domain.shared.EpochTime;
import com.selimssevgi.trxstats.domain.specification.TransactionSpecification;

public final class TestData {
  public static final Amount VALID_AMOUNT = Amount.of(26.0);

  public static final EpochTime OLDER_THAN_ACCEPTED_TRX_TIME_LIMIT =
          EpochTime.fromSecondsAgo(TransactionSpecification.TRX_TIME_LIMIT_IN_SECONDS + 2);

  // cannot be constant, while running all tests,it causes to fail
  public static EpochTime validEpochTime() {
     return EpochTime.fromSecondsAgo(26);
  }

  // cannot be constant, while running all tests,it causes to fail
  public static EpochTime inAcceptedTrxTimeLimit() {
    return EpochTime.fromSecondsAgo(TransactionSpecification.TRX_TIME_LIMIT_IN_SECONDS - 24);
  }

  //TODO:selimssevgi: mathematically not correct
  public static final double SUM = 62.0;
  public static final double MAX = 26.0;
  public static final double MIN = 2.6;
  public static final double AVG = 6.2;
  public static final long COUNT = 62L;
}

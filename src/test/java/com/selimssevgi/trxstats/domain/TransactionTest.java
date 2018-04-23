package com.selimssevgi.trxstats.domain;

import com.selimssevgi.trxstats.domain.shared.EpochTime;
import com.selimssevgi.trxstats.domain.specification.TransactionSpecification;
import com.selimssevgi.trxstats.util.TestData;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Transaction class unit tests.
 */
@RunWith(JUnit4.class)
public class TransactionTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void shouldProvideStaticFactoryMethod() {
    Transaction.of(TestData.VALID_AMOUNT, TestData.validEpochTime());
  }

  @Test
  public void shouldNotAcceptNullAmount() {
    thrown.expect(NullPointerException.class);
    Transaction.of(null, TestData.validEpochTime());
  }

  @Test
  public void shouldNotAcceptNullTime() {
    thrown.expect(NullPointerException.class);
    Transaction.of(TestData.VALID_AMOUNT, null);
  }

  @Test
  public void shouldCreateAValidTrxObject() {
    Transaction transaction =
            Transaction.of(TestData.VALID_AMOUNT, TestData.validEpochTime());

    Assertions.assertThat(transaction).isNotNull();
  }

  @Test
  public void shouldExportAmountAndTime() {
    EpochTime validEpochTime = TestData.validEpochTime();

    Transaction transaction =
            Transaction.of(TestData.VALID_AMOUNT, validEpochTime);

    Assertions.assertThat(transaction.amount()).isSameAs(TestData.VALID_AMOUNT);
    Assertions.assertThat(transaction.time()).isSameAs(validEpochTime);
  }

  @Test
  public void shouldAnswerWhetherOlderThanGivenTime() {
    final long trxTimeLimitInSeconds = TransactionSpecification.TRX_TIME_LIMIT_IN_SECONDS;

    Transaction transaction = Transaction.of(
            TestData.VALID_AMOUNT,
            TestData.OLDER_THAN_ACCEPTED_TRX_TIME_LIMIT);

    Assertions.assertThat(transaction.isOlderThan(trxTimeLimitInSeconds)).isTrue();

    Transaction notOlderTrx = Transaction.of(
            TestData.VALID_AMOUNT,
            TestData.inAcceptedTrxTimeLimit());

    Assertions.assertThat(notOlderTrx.isOlderThan(trxTimeLimitInSeconds)).isFalse();
  }
}
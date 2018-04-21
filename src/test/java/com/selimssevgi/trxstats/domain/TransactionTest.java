package com.selimssevgi.trxstats.domain;

import com.selimssevgi.trxstats.service.TransactionSpecification;
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
    Transaction.of(TestData.VALID_AMOUNT, TestData.VALID_EPOCH_TIME);
  }

  @Test
  public void shouldNotAcceptNullAmount() {
    thrown.expect(NullPointerException.class);
    Transaction.of(null, TestData.VALID_EPOCH_TIME);
  }

  @Test
  public void shouldNotAcceptNullTime() {
    thrown.expect(NullPointerException.class);
    Transaction.of(TestData.VALID_AMOUNT, null);
  }

  @Test
  public void shouldCreateAValidTrxObject() {
    Transaction transaction =
            Transaction.of(TestData.VALID_AMOUNT, TestData.VALID_EPOCH_TIME);

    Assertions.assertThat(transaction).isNotNull();
  }

  @Test
  public void shouldExportAmountAndTime() {
    Transaction transaction =
            Transaction.of(TestData.VALID_AMOUNT, TestData.VALID_EPOCH_TIME);

    Assertions.assertThat(transaction.amount()).isSameAs(TestData.VALID_AMOUNT);
    Assertions.assertThat(transaction.time()).isSameAs(TestData.VALID_EPOCH_TIME);
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
            TestData.IN_ACCEPTED_TRX_TIME_LIMIT);

    Assertions.assertThat(notOlderTrx.isOlderThan(trxTimeLimitInSeconds)).isFalse();
  }
}
package com.selimssevgi.trxstats.service;

import com.selimssevgi.trxstats.domain.Transaction;
import com.selimssevgi.trxstats.domain.shared.Amount;
import com.selimssevgi.trxstats.domain.shared.EpochTime;
import com.selimssevgi.trxstats.repository.TransactionRepository;
import com.selimssevgi.trxstats.service.model.NewTransactionRequest;
import com.selimssevgi.trxstats.service.model.TransactionStatisticsDto;
import com.selimssevgi.trxstats.util.TestData;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

/**
 * Transaction Statistics Service unit tests.
 */
@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceImplTest {

  private TransactionService transactionService;

  private TransactionSpecification transactionSpecification;

  private TransactionRepository transactionRepository;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() {
    transactionSpecification = Mockito.mock(TransactionSpecification.class);
    transactionRepository = Mockito.mock(TransactionRepository.class);
    transactionService =
            new TransactionServiceImpl(transactionRepository, transactionSpecification);
  }

  @Test
  public void shouldNotAcceptNullTransaction() {
    thrown.expect(NullPointerException.class);
    transactionService.add(null);
  }

  @Test
  public void shouldNotAcceptTransactionsOlderThan60Seconds() {
    NewTransactionRequest oldTrx =
            new NewTransactionRequest(
                    TestData.VALID_AMOUNT,
                    TestData.OLDER_THAN_ACCEPTED_TRX_TIME_LIMIT);

    Mockito.doReturn(true)
            .when(transactionSpecification).isNotSatisfiedBy(any());

    thrown.expect(OldTransactionException.class);
    transactionService.add(oldTrx);
  }

  @Test
  public void shouldAcceptValidTransaction() {
    NewTransactionRequest validTrx =
            new NewTransactionRequest(
                    TestData.VALID_AMOUNT,
                    TestData.IN_ACCEPTED_TRX_TIME_LIMIT);

    transactionService.add(validTrx);

    Mockito.verify(transactionRepository, Mockito.only()).save(any());
  }

  @Test
  public void shouldCalculateStatistics() {
    List<Transaction> transactions = Arrays.asList(
            Transaction.of(Amount.of(1.0), EpochTime.fromSecondsAgo(5)),
            Transaction.of(Amount.of(2.0), EpochTime.fromSecondsAgo(5)),
            Transaction.of(Amount.of(3.0), EpochTime.fromSecondsAgo(5)),
            Transaction.of(Amount.of(4.0), EpochTime.fromSecondsAgo(5)),
            Transaction.of(Amount.of(5.0), EpochTime.fromSecondsAgo(5))
    );

    Mockito.doReturn(transactions)
            .when(transactionRepository).findAll();

    final double expectedSum = 15.0;
    final double expectedMax = 5.0;
    final double expectedMin = 1.0;
    final double expectedAvg = 3.0;
    final long expectedCount = 5L;


    TransactionStatisticsDto statisticsDto = transactionService.calculateStatistics();

    Mockito.verify(transactionRepository, Mockito.only()).findAll();

    Assertions.assertThat(statisticsDto.getSum()).isEqualTo(expectedSum);
    Assertions.assertThat(statisticsDto.getMax()).isEqualTo(expectedMax);
    Assertions.assertThat(statisticsDto.getMin()).isEqualTo(expectedMin);
    Assertions.assertThat(statisticsDto.getAvg()).isEqualTo(expectedAvg);
    Assertions.assertThat(statisticsDto.getCount()).isEqualTo(expectedCount);
  }

  @Test
  public void shouldReturnZeroValuedStatisticsWhenNoTransactionFound() {
    final double doubleZeroValue = 0.0d;
    final long longZeroValue = 0L;

    Mockito.doReturn(Collections.EMPTY_LIST)
            .when(transactionRepository).findAll();

    TransactionStatisticsDto statisticsDto = transactionService.calculateStatistics();

    Assertions.assertThat(statisticsDto.getSum()).isEqualTo(doubleZeroValue);
    Assertions.assertThat(statisticsDto.getMax()).isEqualTo(doubleZeroValue);
    Assertions.assertThat(statisticsDto.getMin()).isEqualTo(doubleZeroValue);
    Assertions.assertThat(statisticsDto.getAvg()).isEqualTo(doubleZeroValue);
    Assertions.assertThat(statisticsDto.getCount()).isEqualTo(longZeroValue);
  }
}
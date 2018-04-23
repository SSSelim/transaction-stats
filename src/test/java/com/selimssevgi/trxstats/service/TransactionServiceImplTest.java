package com.selimssevgi.trxstats.service;

import com.selimssevgi.trxstats.domain.specification.TransactionSpecification;
import com.selimssevgi.trxstats.repository.TransactionRepository;
import com.selimssevgi.trxstats.service.model.NewTransactionRequest;
import com.selimssevgi.trxstats.util.TestData;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;

/**
 * Transaction StatisticsCalculatorImpl Service unit tests.
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
                    TestData.inAcceptedTrxTimeLimit());

    transactionService.add(validTrx);

    Mockito.verify(transactionRepository, Mockito.only()).save(any());
  }
}
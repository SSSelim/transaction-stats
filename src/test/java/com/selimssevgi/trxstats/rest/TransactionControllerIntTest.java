package com.selimssevgi.trxstats.rest;

import com.selimssevgi.trxstats.domain.shared.EpochTime;
import com.selimssevgi.trxstats.rest.model.TransactionInput;
import com.selimssevgi.trxstats.rest.model.TransactionStatisticsOutput;
import com.selimssevgi.trxstats.util.TestData;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TransactionControllerIntTest {

  // @LocalServerPort //TODO:selimssevgi: somehow it does not inject random port
  private int port = 6363;

  // @Autowired
  private TestRestTemplate testRestTemplate = new TestRestTemplate();

  private final String URL = "http://localhost:" + port;
  private final String TRX_POST_ENDPOINT = URL + "/transactions";
  private final String STATS_ENDPOINT = URL + "/statistics";

  @Test
  public void givenNotAcceptableTransactionShouldReturn204() {
    TransactionInput notAcceptedTrx = new TransactionInput();
    notAcceptedTrx.setAmount(TestData.VALID_AMOUNT.value());
    notAcceptedTrx.setTimestamp(TestData.OLDER_THAN_ACCEPTED_TRX_TIME_LIMIT.value());

    ResponseEntity<Void> response = testRestTemplate.
            postForEntity(TRX_POST_ENDPOINT, notAcceptedTrx, Void.class);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  @Test
  @DirtiesContext
  public void givenAcceptableTransactionShouldReturn201() {
    TransactionInput valid = new TransactionInput();
    valid.setAmount(TestData.VALID_AMOUNT.value());
    valid.setTimestamp(TestData.inAcceptedTrxTimeLimit().value());

    postTrxAndAssertCreated(valid);
  }

  @Test
  public void givenNoTransactionShouldReturnZeroValueStatistics() {
    TransactionStatisticsOutput stats = requestStatsAndAssertOkAndReturnStats();

    Assertions.assertThat(stats.getSum()).isEqualTo(0.0);
    Assertions.assertThat(stats.getAvg()).isEqualTo(0.0);
    Assertions.assertThat(stats.getMin()).isEqualTo(0.0);
    Assertions.assertThat(stats.getMax()).isEqualTo(0.0);
    Assertions.assertThat(stats.getCount()).isEqualTo(0L);
  }

  @Test
  @DirtiesContext
  public void givenMoreThanOneValidTrxShouldReturnCorrectlyCalculatedStatistics() {
    // given
    TransactionInput validTrx = new TransactionInput();
    validTrx.setAmount(26.0);
    validTrx.setTimestamp(TestData.inAcceptedTrxTimeLimit().value());

    TransactionInput anotherValidTrx = new TransactionInput();
    anotherValidTrx.setAmount(62.0);
    anotherValidTrx.setTimestamp(TestData.inAcceptedTrxTimeLimit().value());

    postTrxAndAssertCreated(validTrx);
    postTrxAndAssertCreated(anotherValidTrx);

    // act
    TransactionStatisticsOutput stats = requestStatsAndAssertOkAndReturnStats();

    // assert
    final long expectedCount = 2L;
    final double expectedSum = validTrx.getAmount() + anotherValidTrx.getAmount();
    final double expectedMin = Math.min(validTrx.getAmount(), anotherValidTrx.getAmount());
    final double expectedMax = Math.max(validTrx.getAmount(), anotherValidTrx.getAmount());
    final double expectedAvg = expectedSum / expectedCount;

    Assertions.assertThat(stats.getCount()).isEqualTo(expectedCount);
    Assertions.assertThat(stats.getSum()).isEqualTo(expectedSum);
    Assertions.assertThat(stats.getMax()).isEqualTo(expectedMax);
    Assertions.assertThat(stats.getMin()).isEqualTo(expectedMin);
    Assertions.assertThat(stats.getAvg()).isEqualTo(expectedAvg);
  }

  //TODO:selimssevgi: it fails with IO exception, when running all tests together
  // another problem is that spring does not show logs.
  // @Test
  // @DirtiesContext
  // public void shouldExcludeTransactionsFromStatisticsWhenTheyGetOutOfTrxTimeLimit()
  //         throws InterruptedException {
  //   // given
  //   TransactionInput trxWithLongLife = new TransactionInput();
  //   trxWithLongLife.setAmount(26.0);
  //   trxWithLongLife.setTimestamp(EpochTime.fromSecondsAgo(1).value());
  //
  //   TransactionInput trxWithShortLife = new TransactionInput();
  //   trxWithShortLife.setAmount(62.0);
  //   trxWithShortLife.setTimestamp(EpochTime.fromSecondsAgo(55).value());
  //
  //   postTrxAndAssertCreated(trxWithLongLife);
  //   postTrxAndAssertCreated(trxWithShortLife);
  //
  //   // simulating passing time, might not be the best way or practise
  //   TimeUnit.SECONDS.sleep(8);
  //
  //   // act
  //   TransactionStatisticsOutput stats = requestStatsAndAssertOkAndReturnStats();
  //
  //   // assert //TODO:selimssevgi: could extract this repeat part to private method
  //   final long expectedCount = 1L;
  //   final double expectedSum = trxWithLongLife.getAmount();
  //   final double expectedMin = trxWithLongLife.getAmount();
  //   final double expectedMax = trxWithLongLife.getAmount();
  //   final double expectedAvg = trxWithLongLife.getAmount();
  //
  //   Assertions.assertThat(stats.getCount()).isEqualTo(expectedCount);
  //   Assertions.assertThat(stats.getSum()).isEqualTo(expectedSum);
  //   Assertions.assertThat(stats.getMax()).isEqualTo(expectedMax);
  //   Assertions.assertThat(stats.getMin()).isEqualTo(expectedMin);
  //   Assertions.assertThat(stats.getAvg()).isEqualTo(expectedAvg);
  // }

  private TransactionStatisticsOutput requestStatsAndAssertOkAndReturnStats() {
    ResponseEntity<TransactionStatisticsOutput> response = testRestTemplate.
            getForEntity(STATS_ENDPOINT, TransactionStatisticsOutput.class);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    return response.getBody();
  }

  private void postTrxAndAssertCreated(TransactionInput validTrx) {
    ResponseEntity<Void> response = testRestTemplate.
            postForEntity(TRX_POST_ENDPOINT, validTrx, Void.class);

    Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
  }
}
package com.selimssevgi.trxstats.domain.shared;


import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * EpochTime class unit tests.
 */
@RunWith(JUnit4.class)
public class EpochTimeTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void shouldNotAcceptNegativeValues() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage(Matchers.containsString("negative"));
    EpochTime.of(-26L);
  }

  @Test
  public void shouldAnswerWhetherGivenEpochTimeIsBefore() {
    EpochTime epochTime = EpochTime.of(5);

    EpochTime later = EpochTime.of(6);
    Assertions.assertThat(epochTime.isBefore(later)).isTrue();

    EpochTime before = EpochTime.of(3);
    Assertions.assertThat(epochTime.isBefore(before)).isFalse();
  }

  @Test
  public void shouldImplementComparable() {
    EpochTime first = EpochTime.of(3);
    EpochTime second = EpochTime.of(5);
    EpochTime alsoFirst = EpochTime.of(3);

    Assertions.assertThat(first.compareTo(second)).isEqualTo(-1);
    Assertions.assertThat(second.compareTo(first)).isEqualTo(1);
    Assertions.assertThat(first.compareTo(alsoFirst)).isEqualTo(0);
  }

  @Test
  public void shouldImplementContentEquality() {
    EpochTime epochTime = EpochTime.of(5);
    EpochTime notEqualTo = EpochTime.of(6);

    Assertions.assertThat(epochTime).isNotEqualTo(notEqualTo);

    EpochTime equalTo = EpochTime.of(5);

    Assertions.assertThat(epochTime).isEqualTo(equalTo);
  }

  @Test
  public void shouldOverrideToString() {
    long timeInMs = 2626262626L;
    EpochTime epochTime = EpochTime.of(timeInMs);
    Assertions.assertThat(epochTime.toString()).contains(String.valueOf(timeInMs));
  }
}
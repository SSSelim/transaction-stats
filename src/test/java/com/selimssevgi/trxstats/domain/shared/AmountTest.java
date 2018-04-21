package com.selimssevgi.trxstats.domain.shared;

import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Amount class unit tests.
 */
@RunWith(JUnit4.class)
public class AmountTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void shouldNotAcceptZero() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage(Matchers.containsString("zero"));
    Amount.of(0.0);
  }

  @Test
  public void shouldNotAcceptNegativeValues() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage(Matchers.containsString("negative"));
    Amount.of(-26.0);
  }

  @Test
  public void shouldImplementContentEquality() {
    Amount amount1 = Amount.of(2.0);
    Amount notEqualAmount = Amount.of(6.0);
    Assertions.assertThat(amount1).isNotEqualTo(notEqualAmount);

    Amount equalAmount = Amount.of(2.0);
    Assertions.assertThat(amount1).isEqualTo(equalAmount);
  }

  @Test
  public void shouldOverrideToString() {
    Amount amount = Amount.of(2.6);
    Assertions.assertThat(amount.toString()).contains("2.6");
  }
}
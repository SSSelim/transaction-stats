package com.selimssevgi.trxstats.domain.specification;

import com.selimssevgi.trxstats.domain.Transaction;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TransactionSpecification extends AbstractSpecification<Transaction> {
  public static final long TRX_TIME_LIMIT_IN_SECONDS = 60L;

  @Override
  public boolean isSatisfiedBy(Transaction transaction) {
    return Optional.ofNullable(transaction)
            .map(trx -> !transaction.isOlderThan(TRX_TIME_LIMIT_IN_SECONDS))
            .orElse(Boolean.FALSE);
  }
}

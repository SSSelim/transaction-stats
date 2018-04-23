package com.selimssevgi.trxstats.repository;

import com.selimssevgi.trxstats.domain.Transaction;
import com.selimssevgi.trxstats.domain.shared.Statistics;

public interface TransactionRepository extends Repository<Transaction> {
  Statistics getStatistics();
}


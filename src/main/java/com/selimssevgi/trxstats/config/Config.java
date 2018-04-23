package com.selimssevgi.trxstats.config;

import com.selimssevgi.trxstats.domain.Transaction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

@Configuration
public class Config {

  @Bean
  public BlockingDeque<Transaction> transactionsCollection() {
    return new LinkedBlockingDeque<>();
  }
}

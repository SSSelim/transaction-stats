package com.selimssevgi.trxstats.rest;

import com.selimssevgi.trxstats.rest.model.TransactionInput;
import com.selimssevgi.trxstats.rest.model.TransactionStatisticsOutput;
import com.selimssevgi.trxstats.service.OldTransactionException;
import com.selimssevgi.trxstats.service.TransactionService;
import com.selimssevgi.trxstats.service.model.NewTransactionRequest;
import com.selimssevgi.trxstats.service.model.TransactionStatisticsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Transaction rest controller.
 */
@RestController
public class TransactionController {
  private static final Logger LOGGER = LoggerFactory.getLogger(TransactionController.class);

  private final TransactionService transactionService;

  public TransactionController(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  /**
   * POST /transactions : Creates a new transaction.
   * <p>
   *   Creates a new transaction if timestamp is not older than 60 seconds.
   * </p>
   *
   * @param transactionInput the user to update
   * @return the ResponseEntity with status 201 if successful,
   *         or status 204 if transaction is older than expected,
   *         both with empty content
   */
  @PostMapping("/transactions")
  public ResponseEntity<Void> createTransaction(@RequestBody TransactionInput transactionInput) {
    NewTransactionRequest newTransactionRequest =
            TransactionMapper.toNewTransactionRequest(transactionInput);

    try {
      transactionService.add(newTransactionRequest);
      LOGGER.debug("Given transaction is accepted, returning 201");
      return ResponseEntity.status(ResponseCodes.TRX_ACCEPTED_SUCCESSFULLY.code()).build();
    } catch (OldTransactionException e) {
      LOGGER.debug("Given transaction is older than 60 seconds, returning 204");
      return ResponseEntity.status(ResponseCodes.TRX_OLDER_THAN_ACCEPTED_LIMIT.code()).build();
    }
  }

  /**
   * GET /statistics : gets statistics of last 60 seconds.
   *
   * @return the ResponseEntity with status 200 (OK) and with body of statistics
   */
  @GetMapping("/statistics")
  public ResponseEntity<TransactionStatisticsOutput> getTransactionStatistics() {
    TransactionStatisticsDto trxStatsDto = transactionService.calculateStatistics();

    TransactionStatisticsOutput trxStatsOutput =
            TransactionMapper.toTransactionStatisticsOutput(trxStatsDto);

    return ResponseEntity.ok(trxStatsOutput);
  }
}

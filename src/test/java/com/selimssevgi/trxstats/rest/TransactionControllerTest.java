package com.selimssevgi.trxstats.rest;

import com.selimssevgi.trxstats.rest.model.TransactionInput;
import com.selimssevgi.trxstats.service.OldTransactionException;
import com.selimssevgi.trxstats.service.TransactionService;
import com.selimssevgi.trxstats.service.model.TransactionStatisticsDto;
import com.selimssevgi.trxstats.util.TestData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TransactionService transactionService;

  @Test
  public void shouldReturn201WhenTransactionAcceptedSuccessfully() throws Exception {
    TransactionInput validTrxInput =
            new TransactionInput(
                    TestData.VALID_AMOUNT.value(),
                    TestData.validEpochTime().value());

    Mockito.doNothing()
            .when(transactionService).add(any());

    mockMvc.perform(
            post("/transactions")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(TestUtil.asJsonBytes(validTrxInput)))
            .andExpect(status().is(ResponseCodes.TRX_ACCEPTED_SUCCESSFULLY.code()));

    Mockito.verify(transactionService, Mockito.only()).add(any());
  }

  @Test
  public void shouldReturn204WhenTransactionIsOlderThan60Seconds() throws Exception {
    TransactionInput trxOlderThan60Seconds = new TransactionInput(
            TestData.VALID_AMOUNT.value(),
            TestData.OLDER_THAN_ACCEPTED_TRX_TIME_LIMIT.value());

    Mockito.doThrow(OldTransactionException.class)
            .when(transactionService).add(any());

    mockMvc.perform(
            post("/transactions")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(TestUtil.asJsonBytes(trxOlderThan60Seconds)))
            .andExpect(status().is(ResponseCodes.TRX_OLDER_THAN_ACCEPTED_LIMIT.code()));

    Mockito.verify(transactionService, Mockito.only()).add(any());
  }

  @Test
  public void shouldReturnStatistics() throws Exception {
    TransactionStatisticsDto trxStatsDto =
            TransactionStatisticsDto.newBuilder()
                    .sum(TestData.SUM)
                    .maximum(TestData.MAX)
                    .minimum(TestData.MIN)
                    .average(TestData.AVG)
                    .count(TestData.COUNT)
                    .build();

    Mockito.doReturn(trxStatsDto)
            .when(transactionService).calculateStatistics();

    mockMvc.perform(
            get("/statistics")
                    .accept(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.sum").value(TestData.SUM))
            .andExpect(jsonPath("$.avg").value(TestData.AVG))
            .andExpect(jsonPath("$.max").value(TestData.MAX))
            .andExpect(jsonPath("$.min").value(TestData.MIN))
            .andExpect(jsonPath("$.count").value(TestData.COUNT));

    Mockito.verify(transactionService, Mockito.only()).calculateStatistics();
  }
}
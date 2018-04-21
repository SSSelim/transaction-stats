package com.selimssevgi.trxstats.rest.model;

/**
 * Client side input object to request a new transaction creation.
 * TODO:selimssevgi: java validation api can be used for validation.
 */
public class TransactionInput {
  private double amount;
  private long timestamp;

  public TransactionInput() {
    // for json mapper
  }

  public TransactionInput(double amount, long timestamp) {
    this.amount = amount;
    this.timestamp = timestamp;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }
}

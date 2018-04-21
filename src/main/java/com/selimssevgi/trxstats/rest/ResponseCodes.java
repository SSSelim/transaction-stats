package com.selimssevgi.trxstats.rest;

/**
 * Http response codes.
 */
public enum ResponseCodes {
  TRX_ACCEPTED_SUCCESSFULY(201),
  TRX_OLDER_THAN_ACCEPTED_LIMIT(204);

  private int statusCode;

  ResponseCodes(int statusCode) {
    this.statusCode = statusCode;
  }

  public int code() {
    return statusCode;
  }
}

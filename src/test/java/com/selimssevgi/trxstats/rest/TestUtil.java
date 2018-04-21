package com.selimssevgi.trxstats.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

class TestUtil {

  /**
   * Convert an object to JSON byte array.
   *
   * @param object the object to convert
   * @return the JSON byte array
   */
  static byte[] asJsonBytes(Object object) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

    return mapper.writeValueAsBytes(object);
  }
}

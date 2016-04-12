package com.xeiam.xchange.gatecoin.dto.trade;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import si.mazi.rescu.ExceptionalReturnContentException;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xeiam.xchange.gatecoin.dto.GatecoinException;
import com.xeiam.xchange.gatecoin.dto.trade.Results.GatecoinCancelOrderResult;

public class CancelOrderJSONTest {

  @Test
  public void testUnmarshal() throws IOException {

    // Read in the JSON from the example resources
    InputStream is = CancelOrderJSONTest.class.getResourceAsStream("/trade/example-cancel-order-pass.json");

    // Use Jackson to parse it
    ObjectMapper mapper = new ObjectMapper();
    GatecoinCancelOrderResult result = mapper.readValue(is, GatecoinCancelOrderResult.class);

    assertThat(result.getResponseStatus().getMessage()).isEqualTo("OK");

    // Read in the JSON from the example resources
    is = CancelOrderJSONTest.class.getResourceAsStream("/trade/example-cancel-order-fail.json");

    // Use Jackson to parse it
    try {
      mapper.readValue(is, GatecoinCancelOrderResult.class);
      assertThat(false).isTrue();
    } catch (JsonMappingException | ExceptionalReturnContentException ignored) { }

    is = CancelOrderJSONTest.class.getResourceAsStream("/trade/example-cancel-order-fail.json");
    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    GatecoinException exceptionResult = mapper.readValue(is, GatecoinException.class);

    assertThat(exceptionResult.getMessage()).contains("Cancel order rejected");
    assertThat(exceptionResult.getErrorCode()).isEqualTo("1010");
  }
}

package com.xeiam.xchange.lakebtc.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xeiam.xchange.lakebtc.dto.LakeBTCResponse;

/**
 * Created by cristian.lucaci on 12/19/2014.
 */
public class LakeBTCOrderResponse extends LakeBTCResponse<LakeBTCOrder> {

  /**
   * Constructor
   *
   * @param id
   * @param result
   */
  public LakeBTCOrderResponse(@JsonProperty("id") String id, @JsonProperty("result") LakeBTCOrder result) {
    super(id, result);
  }
}

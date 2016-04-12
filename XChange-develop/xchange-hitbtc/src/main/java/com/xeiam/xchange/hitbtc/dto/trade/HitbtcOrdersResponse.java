package com.xeiam.xchange.hitbtc.dto.trade;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xeiam.xchange.hitbtc.dto.HitbtcBaseResponse;

public class HitbtcOrdersResponse extends HitbtcBaseResponse {

  HitbtcOrder[] orders;

  public HitbtcOrdersResponse(@JsonProperty("orders") HitbtcOrder[] orders) {

    super();
    this.orders = orders;
  }

  public HitbtcOrder[] getOrders() {

    return orders;
  }

  @Override
  public String toString() {

    StringBuilder builder = new StringBuilder();
    builder.append("HitbtcOrdersResponse [orders=");
    builder.append(Arrays.toString(orders));
    builder.append("]");
    return builder.toString();
  }

}

package com.xeiam.xchange.btcmarkets.dto.trade;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.xeiam.xchange.utils.jackson.BtcToSatoshi;
import com.xeiam.xchange.utils.jackson.MillisecTimestampDeserializer;
import com.xeiam.xchange.utils.jackson.SatoshiToBtc;

public class BTCMarketsUserTrade {

  private Long id;

  private String description;

  @JsonSerialize(using = BtcToSatoshi.class)
  @JsonDeserialize(using = SatoshiToBtc.class)
  private BigDecimal price;

  @JsonSerialize(using = BtcToSatoshi.class)
  @JsonDeserialize(using = SatoshiToBtc.class)
  private BigDecimal volume;

  @JsonSerialize(using = BtcToSatoshi.class)
  @JsonDeserialize(using = SatoshiToBtc.class)
  private BigDecimal fee;

  private BTCMarketsOrder.Side side;

  @JsonDeserialize(using = MillisecTimestampDeserializer.class)
  private Date creationTime;

  public Long getId() {
    return id;
  }

  public String getDescription() {
    return description;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public BigDecimal getVolume() {
    return volume;
  }

  public BigDecimal getFee() {
    return fee;
  }

  public Date getCreationTime() {
    return creationTime;
  }

  public BTCMarketsOrder.Side getSide() {
    return side;
  }

  @Override
  public String toString() {
    return String.format("BTCMarketsUserTrade{id=%d, side='%s', description='%s', price=%s, volume=%s, fee=%s, creationTime=%s}",
        id, side, description, price, volume, fee, creationTime);
  }
}

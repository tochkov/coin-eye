package com.xeiam.xchange.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.Order;
import com.xeiam.xchange.dto.meta.ExchangeMetaData;
import com.xeiam.xchange.dto.meta.MarketMetaData;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.dto.trade.MarketOrder;

/**
 * Top of the hierarchy abstract class for an "exchange service"
 */
public abstract class BaseExchangeService {

  /**
   * The base Exchange. Every service has access to the containing exchange class, which hold meta data and the exchange specification
   */
  protected final Exchange exchange;

  /**
   * Constructor
   */
  protected BaseExchangeService(Exchange exchange) {

    this.exchange = exchange;
  }

  public void verifyOrder(LimitOrder limitOrder) {
    ExchangeMetaData exchangeMetaData = exchange.getMetaData();
    verifyOrder(limitOrder, exchangeMetaData);
    BigDecimal price = limitOrder.getLimitPrice().stripTrailingZeros();

    if (price.scale() > exchangeMetaData.getMarketMetaDataMap().get(limitOrder.getCurrencyPair()).getPriceScale()) {
      throw new IllegalArgumentException("Unsupported price scale " + price.scale());
    }
  }

  public void verifyOrder(MarketOrder marketOrder) {
    verifyOrder(marketOrder, exchange.getMetaData());
  }

  final protected void verifyOrder(Order order, ExchangeMetaData exchangeMetaData) {
    MarketMetaData metaData = exchangeMetaData.getMarketMetaDataMap().get(order.getCurrencyPair());
    if (metaData == null) {
      throw new IllegalArgumentException("Invalid CurrencyPair");
    }

    BigDecimal tradableAmount = order.getTradableAmount();
    if (tradableAmount == null)
      throw new IllegalArgumentException("Missing tradableAmount");

    BigDecimal amount = tradableAmount.stripTrailingZeros();
    BigDecimal minimumAmount = metaData.getMinimumAmount();
    if (amount.scale() > minimumAmount.scale()) {
      throw new IllegalArgumentException("Unsupported amount scale " + amount.scale());
    } else if (amount.compareTo(minimumAmount) < 0) {
      throw new IllegalArgumentException("Order amount less than minimum");
    }
  }

  public List<CurrencyPair> getExchangeSymbols() throws IOException {
    return new ArrayList<CurrencyPair>(exchange.getMetaData().getMarketMetaDataMap().keySet());
  }
}

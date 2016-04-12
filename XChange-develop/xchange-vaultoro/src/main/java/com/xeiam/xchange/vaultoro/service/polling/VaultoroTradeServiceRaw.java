package com.xeiam.xchange.vaultoro.service.polling;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.Order.OrderType;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.exceptions.ExchangeException;
import com.xeiam.xchange.vaultoro.VaultoroException;
import com.xeiam.xchange.vaultoro.dto.trade.VaultoroCancelOrderResponse;
import com.xeiam.xchange.vaultoro.dto.trade.VaultoroNewOrderResponse;
import com.xeiam.xchange.vaultoro.dto.trade.VaultoroOpenOrder;
import com.xeiam.xchange.vaultoro.dto.trade.VaultoroOrdersResponse;

public class VaultoroTradeServiceRaw extends VaultoroBasePollingService {

  /**
   * Constructor
   *
   * @param exchange
   */
  public VaultoroTradeServiceRaw(Exchange exchange) {

    super(exchange);
  }

  public VaultoroCancelOrderResponse cancelVaultoroOrder(String orderId) throws IOException {

    try {
      VaultoroCancelOrderResponse response = vaultoro.cancel(orderId, exchange.getNonceFactory(), apiKey, signatureCreator);
      return response;
    } catch (VaultoroException e) {
      throw new ExchangeException(e.getMessage());
    }
  }

  public Map<String, List<VaultoroOpenOrder>> getVaultoroOrders() throws IOException {

    try {
      VaultoroOrdersResponse response = vaultoro.getOrders(exchange.getNonceFactory(), apiKey, signatureCreator);
      return response.getData().get(0);
    } catch (VaultoroException e) {
      throw new ExchangeException(e.getMessage());
    }
  }

  public VaultoroNewOrderResponse placeLimitOrder(CurrencyPair currencyPair, OrderType orderType, BigDecimal amount, BigDecimal price) throws IOException {

    return placeOrder("limit", currencyPair, orderType, amount, price);

  }

  public VaultoroNewOrderResponse placeMarketOrder(CurrencyPair currencyPair, OrderType orderType, BigDecimal amount) throws IOException {

    return placeOrder("market", currencyPair, orderType, amount, null);

  }

  private VaultoroNewOrderResponse placeOrder(String type, CurrencyPair currencyPair, OrderType orderType, BigDecimal amount, BigDecimal price) throws IOException {

    String baseSymbol = currencyPair.base.getCurrencyCode().toLowerCase();

    if (orderType == OrderType.BID) {

      if (price == null) {

        VaultoroMarketDataService ds = new VaultoroMarketDataService(exchange);
        OrderBook orderBook = ds.getOrderBook(currencyPair);
        List<LimitOrder> asks = orderBook.getAsks();

        if (!asks.isEmpty()) {
          LimitOrder lowestAsk = orderBook.getAsks().get(0);
          price = lowestAsk.getLimitPrice();
        }
        else {
          price = ds.getLast(currencyPair);
        }
      }
      else {
        amount = price.multiply(amount, new MathContext(8, RoundingMode.HALF_DOWN));
      }
      try {
        return vaultoro.buy(baseSymbol, type, exchange.getNonceFactory(), apiKey, amount, price, signatureCreator);
      } catch (VaultoroException e) {
        throw new ExchangeException(e.getMessage());
      }
    }
    else {
      try {
        return vaultoro.sell(baseSymbol, type, exchange.getNonceFactory(), apiKey, amount, price, signatureCreator);
      } catch (VaultoroException e) {
        throw new ExchangeException(e.getMessage());
      }
    }

  }
}
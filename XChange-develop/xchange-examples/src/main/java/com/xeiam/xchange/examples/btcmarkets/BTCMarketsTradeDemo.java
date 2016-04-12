package com.xeiam.xchange.examples.btcmarkets;

import java.math.BigDecimal;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.btcmarkets.service.polling.BTCMarketsTradeService;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.Order.OrderType;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.dto.trade.MarketOrder;
import com.xeiam.xchange.dto.trade.UserTrades;
import com.xeiam.xchange.service.polling.trade.PollingTradeService;

public class BTCMarketsTradeDemo {

  public static void main(String[] args) throws Exception {
    Exchange btcMarketsExchange = BTCMarketsExampleUtils.createTestExchange();
    PollingTradeService tradeService = btcMarketsExchange.getPollingTradeService();

    System.out.println("Open Orders: " + tradeService.getOpenOrders());

    // Place a limit sell order at a high price
    LimitOrder sellOrder = new LimitOrder((OrderType.ASK), new BigDecimal("0.003"), CurrencyPair.BTC_AUD, null, null, new BigDecimal("2000"));
    String limitOrderReturnValue = tradeService.placeLimitOrder(sellOrder);
    System.out.println("Limit Order return value: " + limitOrderReturnValue);

    System.out.println("Open Orders: " + tradeService.getOpenOrders());

    // Cancel the added order.
    boolean cancelResult = tradeService.cancelOrder(limitOrderReturnValue);
    System.out.println("Canceling returned " + cancelResult);
    System.out.println("Open Orders: " + tradeService.getOpenOrders());

    // An example of a sell market order
    MarketOrder sellMarketOrder = new MarketOrder((OrderType.ASK), new BigDecimal("0.003"), CurrencyPair.BTC_AUD, null, null);
    String marketSellOrderId = tradeService.placeMarketOrder(sellMarketOrder);
    System.out.println("Market Order return value: " + marketSellOrderId);

    // An example of a buy limit order.
    LimitOrder buyOrder = new LimitOrder((OrderType.BID), new BigDecimal("0.002"), CurrencyPair.BTC_AUD, null, null, new BigDecimal("240"));
    String buyLimiOrderId = tradeService.placeLimitOrder(buyOrder);
    System.out.println("Limit Order return value: " + buyLimiOrderId);

    // An example of a buy market order
    MarketOrder buyMarketOrder = new MarketOrder((OrderType.BID), new BigDecimal("0.004"), CurrencyPair.BTC_AUD, null, null);
    String buyMarketOrderId = tradeService.placeMarketOrder(buyMarketOrder);
    System.out.println("Market Order return value: " + buyMarketOrderId);

    BTCMarketsTradeService.HistoryParams params = (BTCMarketsTradeService.HistoryParams) tradeService.createTradeHistoryParams();
    params.setPageLength(10);
    final UserTrades tradeHistory = tradeService.getTradeHistory(params);

    System.out.println("Trade history: " + tradeHistory);
  }
}

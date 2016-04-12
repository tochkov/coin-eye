package com.xeiam.xchange.examples.btcmarkets;

import java.io.IOException;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.btcmarkets.BTCMarketsExchange;
import com.xeiam.xchange.btcmarkets.dto.marketdata.BTCMarketsOrderBook;
import com.xeiam.xchange.btcmarkets.service.polling.BTCMarketsMarketDataServiceRaw;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.service.polling.marketdata.PollingMarketDataService;

public class BTCMarketsMarketDataDemo {

  public static void main(String[] args) throws IOException {
    // Use the factory to get BTCMarkets exchange API using default settings
    Exchange btcMarketsExchange = ExchangeFactory.INSTANCE.createExchange(BTCMarketsExchange.class.getName());
    generic(btcMarketsExchange);
    raw(btcMarketsExchange);
  }

  private static void generic(Exchange btcMarketsExchange) throws IOException {
    // Interested in the public polling market data feed (no authentication)
    PollingMarketDataService btcMarketsMarketDataService = btcMarketsExchange.getPollingMarketDataService();

    // Get the (daily) ticker
    System.out.println("Ticker: " + btcMarketsMarketDataService.getTicker(CurrencyPair.BTC_AUD));

    OrderBook orderBook = btcMarketsMarketDataService.getOrderBook(CurrencyPair.BTC_AUD);
    System.out.println(orderBook.toString());
    System.out.println("full orderbook size: " + (orderBook.getAsks().size() + orderBook.getBids().size()));
    System.out.println("First 10 offers:");
    java.util.List<LimitOrder> asks = orderBook.getAsks();
    for (int i = 0; i < asks.size() && i < 10; i++) {
      System.out.println(asks.get(i));
    }
  }

  private static void raw(Exchange btcMarketsExchange) throws IOException {
    // Interested in the public polling market data feed (no authentication)
    BTCMarketsMarketDataServiceRaw btcMarketsMarketDataService = (BTCMarketsMarketDataServiceRaw) btcMarketsExchange.getPollingMarketDataService();

    // Get the weekly ticker
    System.out.println(
        "Ticker: " + btcMarketsMarketDataService.getBTCMarketsTicker(CurrencyPair.BTC_AUD));

    // Get the latest full order book data
    BTCMarketsOrderBook depth = btcMarketsMarketDataService.getBTCMarketsOrderBook(CurrencyPair.BTC_AUD);
    System.out.println(depth.toString());
    System.out.println("offers: " + (depth.getAsks().size()));
  }
}

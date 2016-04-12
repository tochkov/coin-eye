package com.xeiam.xchange.bitmarket;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.xeiam.xchange.bitmarket.dto.account.BitMarketAccountInfoResponse;
import com.xeiam.xchange.bitmarket.dto.marketdata.BitMarketOrderBook;
import com.xeiam.xchange.bitmarket.dto.marketdata.BitMarketTicker;
import com.xeiam.xchange.bitmarket.dto.marketdata.BitMarketTrade;
import com.xeiam.xchange.bitmarket.dto.trade.BitMarketHistoryOperationsResponse;
import com.xeiam.xchange.bitmarket.dto.trade.BitMarketHistoryTradesResponse;
import com.xeiam.xchange.bitmarket.dto.trade.BitMarketOrdersResponse;
import com.xeiam.xchange.currency.Currency;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.account.Balance;
import com.xeiam.xchange.dto.account.Wallet;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.xeiam.xchange.dto.marketdata.Trade;
import com.xeiam.xchange.dto.marketdata.Trades;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.dto.trade.OpenOrders;
import com.xeiam.xchange.dto.trade.UserTrade;
import com.xeiam.xchange.dto.trade.UserTrades;

/**
 * @author kfonal
 */
public class BitMarketAdaptersTest extends BitMarketTestSupport {

  @Test
  public void testAccountInfoAdapter() throws IOException {
    // given
    BitMarketAccountInfoResponse response = parse("account/example-info-data", BitMarketAccountInfoResponse.class);
    final Balance[] expectedInfoBalances = expectedInfoBalances();

    // when
    Wallet wallet = BitMarketAdapters.adaptWallet(response.getData().getBalance());

    // then
    Map<Currency, Balance> balances = wallet.getBalances();

    assertThat(balances).hasSize(3);
    for (Balance balance : expectedInfoBalances) {
      BitMarketAssert.assertEquals(balances.get(balance.getCurrency()), balance);
      assertThat(wallet.toString()).contains(balance.toString());
    }
  }

  @Test
  public void testOpenOrdersAdapter() throws IOException {
    // given
    BitMarketOrdersResponse response = parse("trade/example-orders-data", BitMarketOrdersResponse.class);
    final LimitOrder[] expectedOrders = expectedOrders();

    // when
    OpenOrders orders = BitMarketAdapters.adaptOpenOrders(response.getData());
    List<LimitOrder> openOrders = orders.getOpenOrders();

    // then
    assertThat(openOrders).hasSize(2);
    for (int i = 0; i < openOrders.size(); i++) {
      BitMarketAssert.assertEquals(openOrders.get(i), expectedOrders[i]);
      assertThat(orders.toString()).contains(expectedOrders[i].toString());
    }
  }

  @Test
  public void testTradeHistoryAdapter() throws IOException {

    // given
    BitMarketHistoryTradesResponse historyTradesResponse = parse("trade/example-history-trades-data", BitMarketHistoryTradesResponse.class);
    BitMarketHistoryOperationsResponse marketHistoryOperationsResponse = parse("trade/example-history-operations-data",
        BitMarketHistoryOperationsResponse.class);

    final UserTrade[] expectedUserTrades = expectedUserTrades();

    // when
    UserTrades trades = BitMarketAdapters.adaptTradeHistory(historyTradesResponse.getData(), marketHistoryOperationsResponse.getData());
    List<UserTrade> userTrades = trades.getUserTrades();

    // then
    assertThat(userTrades).hasSize(5);
    for (int i = 0; i < userTrades.size(); i++) {
      BitMarketAssert.assertEquals(userTrades.get(i), expectedUserTrades[i]);
    }
  }

  @Test
  public void testTickerAdapter() throws IOException {

    // given
    BitMarketTicker bitMarketTicker = parse("marketdata/example-ticker-data", BitMarketTicker.class);

    // when
    Ticker ticker = BitMarketAdapters.adaptTicker(bitMarketTicker, CurrencyPair.BTC_AUD);

    // then
    BitMarketAssert.assertEquals(ticker, TICKER);
  }

  // https://www.bitmarket.pl/json/LTCPLN/trades.json example has addition field 'type' which is not specified in API description (https://www.bitmarket.net/docs.php?file=api_public.html)
  // should be changed after issue #1141 fix
  @Test
  public void testTradesAdapter() throws IOException {
    // given
    BitMarketTrade[] bitMarketTrades = parse("marketdata/example-trades-data", BitMarketTrade[].class);
    final Trade[] expectedTrades = expectedTrades();

    // when
    Trades trades = BitMarketAdapters.adaptTrades(bitMarketTrades, CurrencyPair.BTC_AUD);
    List<Trade> tradeList = trades.getTrades();

    // then
    assertThat(tradeList).hasSize(3);
    for (int i = 0; i < tradeList.size(); i++) {
      BitMarketAssert.assertEquals(tradeList.get(i), expectedTrades[i]);
    }
  }

  @Test
  public void testOrderBookAdapter() throws IOException {
    // given
    BitMarketOrderBook bitMarketOrderBook = parse("marketdata/example-order-book-data", BitMarketOrderBook.class);

    // when
    OrderBook orderBook = BitMarketAdapters.adaptOrderBook(bitMarketOrderBook, CurrencyPair.BTC_AUD);

    // then
    BitMarketAssert.assertEquals(orderBook, ORDER_BOOK);
  }

}

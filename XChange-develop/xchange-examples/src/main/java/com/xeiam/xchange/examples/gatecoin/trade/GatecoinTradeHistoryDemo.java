package com.xeiam.xchange.examples.gatecoin.trade;

import java.io.IOException;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.dto.trade.UserTrades;
import com.xeiam.xchange.examples.gatecoin.GatecoinDemoUtils;
import com.xeiam.xchange.gatecoin.dto.trade.GatecoinTradeHistory;
import com.xeiam.xchange.gatecoin.dto.trade.Results.GatecoinTradeHistoryResult;
import com.xeiam.xchange.gatecoin.service.polling.GatecoinTradeService;
import com.xeiam.xchange.gatecoin.service.polling.GatecoinTradeServiceRaw;
import com.xeiam.xchange.service.polling.trade.PollingTradeService;

/**
 * @author sumedha
 */
public class GatecoinTradeHistoryDemo {

  public static void main(String[] args) throws IOException {
    Exchange gatecoin = GatecoinDemoUtils.createExchange();
    PollingTradeService tradeService = gatecoin.getPollingTradeService();

    generic(tradeService);
    raw((GatecoinTradeServiceRaw) tradeService);
  }

  private static void generic(PollingTradeService tradeService) throws IOException {

    UserTrades result = tradeService.getTradeHistory(tradeService.createTradeHistoryParams());
    System.out.println("Trade history returned " + result);

    //with count parameter
    result = tradeService.getTradeHistory(new GatecoinTradeService.GatecoinTradeHistoryParams(10));
    System.out.println("Trade history returned " + result);

    //with count and tradeId parameter
    result = tradeService.getTradeHistory(new GatecoinTradeService.GatecoinTradeHistoryParams(10, "24561"));
    System.out.println("Trade history returned " + result);
  }

  private static void raw(GatecoinTradeServiceRaw tradeService) throws IOException {

    printRawTradeHistory(tradeService);
  }

  private static void printRawTradeHistory(GatecoinTradeServiceRaw tradeService) throws IOException {

    GatecoinTradeHistoryResult tradeHistoryResult = tradeService.getGatecoinUserTrades();
    System.out.println("Trades: " + tradeHistoryResult.getTransactions().length);
    for (GatecoinTradeHistory trade : tradeHistoryResult.getTransactions()) {
      System.out.println(trade.toString());
    }
  }
}

package com.xeiam.xchange.gatecoin.service.polling;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.gatecoin.GatecoinAuthenticated;
import com.xeiam.xchange.gatecoin.dto.trade.Results.GatecoinCancelOrderResult;
import com.xeiam.xchange.gatecoin.dto.trade.Results.GatecoinOrderResult;
import com.xeiam.xchange.gatecoin.dto.trade.Results.GatecoinPlaceOrderResult;
import com.xeiam.xchange.gatecoin.dto.trade.Results.GatecoinTradeHistoryResult;
import com.xeiam.xchange.gatecoin.service.GatecoinDigest;
import si.mazi.rescu.RestProxyFactory;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author sumedha
 */
public class GatecoinTradeServiceRaw extends GatecoinBasePollingService {
  private final GatecoinAuthenticated gatecoinAuthenticated;
  private final GatecoinDigest signatureCreator;

  public GatecoinTradeServiceRaw(Exchange exchange) {

    super(exchange);
    this.gatecoinAuthenticated = RestProxyFactory.createProxy(GatecoinAuthenticated.class, exchange.getExchangeSpecification().getSslUri());
    this.signatureCreator = GatecoinDigest.createInstance(exchange.getExchangeSpecification().getSecretKey());
  }

  public GatecoinOrderResult getGatecoinOpenOrders() throws IOException {

    return gatecoinAuthenticated.getOpenOrders(exchange.getExchangeSpecification().getApiKey(), signatureCreator, getNow());
  }

  public GatecoinPlaceOrderResult placeGatecoinOrder(
      BigDecimal tradableAmount,
      BigDecimal price,
      String way,
      String code
  ) throws IOException {
    return gatecoinAuthenticated.placeOrder(exchange.getExchangeSpecification().getApiKey(), signatureCreator, getNow(), tradableAmount,
        price, way, code);
  }

  public GatecoinCancelOrderResult cancelGatecoinOrder(String orderId) throws IOException {

    return gatecoinAuthenticated.cancelOrder(exchange.getExchangeSpecification().getApiKey(), signatureCreator, getNow(), orderId);
  }

  public GatecoinCancelOrderResult cancelAllGatecoinOrders() throws IOException {

    return gatecoinAuthenticated.cancelAllOrders(exchange.getExchangeSpecification().getApiKey(), signatureCreator, getNow());
  }

  public GatecoinTradeHistoryResult getGatecoinUserTrades(Integer count, Long transactionId) throws IOException {
    return gatecoinAuthenticated.getUserTrades(exchange.getExchangeSpecification().getApiKey(), signatureCreator, getNow(),
        count, transactionId);
  }

  public GatecoinTradeHistoryResult getGatecoinUserTrades(int count) throws IOException {
    return gatecoinAuthenticated.getUserTrades(exchange.getExchangeSpecification().getApiKey(), signatureCreator, getNow(),
        count);
  }

  public GatecoinTradeHistoryResult getGatecoinUserTrades() throws IOException {
    return gatecoinAuthenticated.getUserTrades(exchange.getExchangeSpecification().getApiKey(), signatureCreator, getNow());
  }
}

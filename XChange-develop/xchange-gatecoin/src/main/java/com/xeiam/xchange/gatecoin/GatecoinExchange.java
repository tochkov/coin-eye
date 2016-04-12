package com.xeiam.xchange.gatecoin;

import com.xeiam.xchange.BaseExchange;
import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.gatecoin.service.polling.GatecoinAccountService;
import com.xeiam.xchange.gatecoin.service.polling.GatecoinMarketDataService;
import com.xeiam.xchange.gatecoin.service.polling.GatecoinTradeService;
import com.xeiam.xchange.service.streaming.ExchangeStreamingConfiguration;
import com.xeiam.xchange.service.streaming.StreamingExchangeService;
import com.xeiam.xchange.utils.nonce.CurrentTimeNonceFactory;

import si.mazi.rescu.SynchronizedValueFactory;

/**
 * @author Sumedha
 */
public class GatecoinExchange extends BaseExchange implements Exchange {

  private SynchronizedValueFactory<Long> nonceFactory = new CurrentTimeNonceFactory();

  @Override
  public void applySpecification(ExchangeSpecification exchangeSpecification) {

    super.applySpecification(exchangeSpecification);
  }

  @Override
  protected void initServices() {

    this.pollingMarketDataService = new GatecoinMarketDataService(this);
    this.pollingTradeService = new GatecoinTradeService(this);
    this.pollingAccountService = new GatecoinAccountService(this);
  }

  @Override
  public ExchangeSpecification getDefaultExchangeSpecification() {

    ExchangeSpecification exchangeSpecification = new ExchangeSpecification(this.getClass().getCanonicalName());
    exchangeSpecification.setSslUri("https://www.gatecoin.com");
    exchangeSpecification.setHost("www.gatecoin.com");
    exchangeSpecification.setPort(80);
    exchangeSpecification.setExchangeName("Gatecoin");
    return exchangeSpecification;
  }

  @Override
  public StreamingExchangeService getStreamingExchangeService(ExchangeStreamingConfiguration configuration) {

    throw new IllegalArgumentException("Gatecoin only supports GatecoinStreamingConfiguration");
  }

  @Override
  public SynchronizedValueFactory<Long> getNonceFactory() {

    return nonceFactory;
  }

}

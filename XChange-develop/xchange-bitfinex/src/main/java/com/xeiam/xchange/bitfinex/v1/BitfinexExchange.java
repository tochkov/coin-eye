package com.xeiam.xchange.bitfinex.v1;

import com.xeiam.xchange.BaseExchange;
import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.bitfinex.v1.service.polling.BitfinexAccountService;
import com.xeiam.xchange.bitfinex.v1.service.polling.BitfinexMarketDataService;
import com.xeiam.xchange.bitfinex.v1.service.polling.BitfinexTradeService;
import com.xeiam.xchange.utils.nonce.AtomicLongIncrementalTime2013NonceFactory;

import si.mazi.rescu.SynchronizedValueFactory;

public class BitfinexExchange extends BaseExchange implements Exchange {

  private SynchronizedValueFactory<Long> nonceFactory = new AtomicLongIncrementalTime2013NonceFactory();

  @Override
  protected void initServices() {
    this.pollingMarketDataService = new BitfinexMarketDataService(this);
    this.pollingAccountService = new BitfinexAccountService(this);
    this.pollingTradeService = new BitfinexTradeService(this);
  }

  @Override
  public ExchangeSpecification getDefaultExchangeSpecification() {

    ExchangeSpecification exchangeSpecification = new ExchangeSpecification(this.getClass().getCanonicalName());
    exchangeSpecification.setSslUri("https://api.bitfinex.com/");
    exchangeSpecification.setHost("api.bitfinex.com");
    exchangeSpecification.setPort(80);
    exchangeSpecification.setExchangeName("BitFinex");
    exchangeSpecification.setExchangeDescription("BitFinex is a bitcoin exchange.");


//    ExchangeSpecification exchangeSpecification = new ExchangeSpecification(this.getClass().getCanonicalName());
//    exchangeSpecification.setSslUri("https://api.bitfinex.com/v1");
//    exchangeSpecification.setHost("api.bitfinex.com");
//    exchangeSpecification.setPort(80);
//    exchangeSpecification.set;
//    exchangeSpecification.setExchangeName("BitFinex");
//    exchangeSpecification.setExchangeDescription("BitFinex is a bitcoin exchange.");



    return exchangeSpecification;
  }

  @Override
  public SynchronizedValueFactory<Long> getNonceFactory() {

    return nonceFactory;
  }
}

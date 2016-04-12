package com.xeiam.xchange.lakebtc;

import com.xeiam.xchange.BaseExchange;
import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.lakebtc.service.polling.LakeBTCAccountService;
import com.xeiam.xchange.lakebtc.service.polling.LakeBTCMarketDataService;
import com.xeiam.xchange.lakebtc.service.polling.LakeBTCTradeService;
import com.xeiam.xchange.utils.nonce.CurrentNanosecondTimeIncrementalNonceFactory;

import si.mazi.rescu.SynchronizedValueFactory;

/**
 * @author kpysniak
 */
public class LakeBTCExchange extends BaseExchange implements Exchange {

  private SynchronizedValueFactory<Long> nonceFactory = new CurrentNanosecondTimeIncrementalNonceFactory();

  @Override
  protected void initServices() {
    this.pollingMarketDataService = new LakeBTCMarketDataService(this);
    this.pollingAccountService = new LakeBTCAccountService(this);
    this.pollingTradeService = new LakeBTCTradeService(this);
  }

  @Override
  public ExchangeSpecification getDefaultExchangeSpecification() {

    ExchangeSpecification exchangeSpecification = new ExchangeSpecification(this.getClass().getCanonicalName());
    exchangeSpecification.setSslUri("https://www.lakebtc.com/");
    exchangeSpecification.setHost("https://lakebtc.com");
    exchangeSpecification.setPort(80);
    exchangeSpecification.setExchangeName("LakeBTC");
    exchangeSpecification.setExchangeDescription("LakeBTC is a Bitcoin exchange for USD and CNY.");

    return exchangeSpecification;
  }

  @Override
  public SynchronizedValueFactory<Long> getNonceFactory() {

    return nonceFactory;
  }
}

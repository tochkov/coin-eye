package com.xeiam.xchange.virtex.v2;

import com.xeiam.xchange.BaseExchange;
import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.virtex.v2.service.polling.VirtExMarketDataService;

import si.mazi.rescu.SynchronizedValueFactory;

public class VirtExExchange extends BaseExchange implements Exchange {

  @Override
  protected void initServices() {
    this.pollingMarketDataService = new VirtExMarketDataService(this);
  }

  @Override
  public ExchangeSpecification getDefaultExchangeSpecification() {

    ExchangeSpecification exchangeSpecification = new ExchangeSpecification(this.getClass().getCanonicalName());
    exchangeSpecification.setSslUri("https://cavirtex.com");
    exchangeSpecification.setHost("cavirtex.com");
    exchangeSpecification.setPort(80);
    exchangeSpecification.setExchangeName("CaVirtEx");
    exchangeSpecification.setExchangeDescription("CAVirtex is a Bitcoin exchange registered in Canada.");

    return exchangeSpecification;
  }

  @Override
  public SynchronizedValueFactory<Long> getNonceFactory() {
    // No private API implemented. Not needed for this exchange at the moment.
    return null;
  }
}

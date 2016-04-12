package com.xeiam.xchange.btccentral;

import com.xeiam.xchange.BaseExchange;
import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.btccentral.service.polling.BTCCentralMarketDataService;

import si.mazi.rescu.SynchronizedValueFactory;

/**
 * @author kpysniak
 */
public class BTCCentralExchange extends BaseExchange implements Exchange {

  @Override
  public ExchangeSpecification getDefaultExchangeSpecification() {

    ExchangeSpecification exchangeSpecification = new ExchangeSpecification(this.getClass().getCanonicalName());
    exchangeSpecification.setSslUri("https://bitcoin-central.net/api/v1/data/eur");
    exchangeSpecification.setHost("bitcoin-central.net");
    exchangeSpecification.setPort(80);
    exchangeSpecification.setExchangeName("Bitcoin-Central");
    exchangeSpecification
        .setExchangeDescription("Bitcoin-Central is a Bitcoin exchange registered and maintained by a company based in Paris, France.");

    return exchangeSpecification;
  }

  @Override
  protected void initServices() {
    this.pollingMarketDataService = new BTCCentralMarketDataService(this);
  }

  @Override
  public SynchronizedValueFactory<Long> getNonceFactory() {
    // No private API implemented. Not needed for this exchange at the moment.
    return null;
  }
}

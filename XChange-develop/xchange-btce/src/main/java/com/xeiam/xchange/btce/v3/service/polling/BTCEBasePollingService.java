package com.xeiam.xchange.btce.v3.service.polling;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.btce.v3.BTCEAuthenticated;
import com.xeiam.xchange.btce.v3.dto.BTCEReturn;
import com.xeiam.xchange.btce.v3.service.BTCEHmacPostBodyDigest;
import com.xeiam.xchange.exceptions.ExchangeException;
import com.xeiam.xchange.exceptions.FundsExceededException;
import com.xeiam.xchange.exceptions.NonceException;
import com.xeiam.xchange.service.BaseExchangeService;
import com.xeiam.xchange.service.polling.BasePollingService;

import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.RestProxyFactory;

public class BTCEBasePollingService extends BaseExchangeService implements BasePollingService {

  private static final String ERR_MSG_NONCE = "invalid nonce parameter; on key:";
  private static final String ERR_MSG_FUNDS = "It is not enough ";

  protected final String apiKey;
  protected final BTCEAuthenticated btce;
  protected final ParamsDigest signatureCreator;

  /**
   * Constructor
   *
   * @param exchange
   */
  public BTCEBasePollingService(Exchange exchange) {

    super(exchange);

    this.btce = RestProxyFactory.createProxy(BTCEAuthenticated.class, exchange.getExchangeSpecification().getSslUri());
    this.apiKey = exchange.getExchangeSpecification().getApiKey();
    this.signatureCreator = BTCEHmacPostBodyDigest.createInstance(exchange.getExchangeSpecification().getSecretKey());
  }

  protected void checkResult(BTCEReturn<?> result) {
    String error = result.getError();

    if (!result.isSuccess()) {
      if (error != null) {
        if (error.startsWith(ERR_MSG_NONCE)) {
          throw new NonceException(error);
        } else if (error.startsWith(ERR_MSG_FUNDS)) {
          throw new FundsExceededException(error);
        }
      }
      throw new ExchangeException(error);
    }

    else if (result.getReturnValue() == null) {
      throw new ExchangeException("Didn't receive any return value. Message: " + error);
    }

    else if (error != null) {
      throw new ExchangeException(error);
    }
  }

}

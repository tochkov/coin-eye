package com.xeiam.xchange.bitstamp.service.polling;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.bitstamp.BitstampAdapters;
import com.xeiam.xchange.bitstamp.dto.account.BitstampDepositAddress;
import com.xeiam.xchange.bitstamp.dto.account.BitstampWithdrawal;
import com.xeiam.xchange.currency.Currency;
import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.service.polling.account.PollingAccountService;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author Matija Mazi
 */
public class BitstampAccountService extends BitstampAccountServiceRaw implements PollingAccountService {

  /**
   * Constructor
   *
   * @param exchange
   */
  public BitstampAccountService(Exchange exchange) {

    super(exchange);
  }

  @Override
  public AccountInfo getAccountInfo() throws IOException {

    return BitstampAdapters.adaptAccountInfo(getBitstampBalance(), exchange.getExchangeSpecification().getUserName());
  }

  @Override
  public String withdrawFunds(Currency currency, BigDecimal amount, String address) throws IOException {

    final BitstampWithdrawal response = withdrawBitstampFunds(currency, amount, address);
    if (response.getId() == null) {
      return null;
    }
    return Integer.toString(response.getId());
  }

  /**
   * This returns the currently set deposit address. It will not generate a new address (ie. repeated calls will return the same address).
   */
  @Override
  public String requestDepositAddress(Currency currency, String... arguments) throws IOException {

    final BitstampDepositAddress response = getBitstampBitcoinDepositAddress();
    return response.getDepositAddress();

  }
}

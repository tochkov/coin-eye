package com.xeiam.xchange.itbit.v1.service.polling;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.itbit.v1.ItBitAdapters;
import com.xeiam.xchange.itbit.v1.dto.account.ItBitAccountInfoReturn;
import com.xeiam.xchange.itbit.v1.dto.account.ItBitDepositRequest;
import com.xeiam.xchange.itbit.v1.dto.account.ItBitDepositResponse;
import com.xeiam.xchange.itbit.v1.dto.account.ItBitWithdrawalRequest;
import com.xeiam.xchange.itbit.v1.dto.account.ItBitWithdrawalResponse;

public class ItBitAccountServiceRaw extends ItBitBasePollingService {

  private final String userId;
  private final String walletId;

  /**
   * Constructor
   *
   * @param exchange
   */
  public ItBitAccountServiceRaw(Exchange exchange) {

    super(exchange);

    this.userId = (String) exchange.getExchangeSpecification().getExchangeSpecificParametersItem("userId");
    this.walletId = (String) exchange.getExchangeSpecification().getExchangeSpecificParametersItem("walletId");
  }

  public ItBitAccountInfoReturn[] getItBitAccountInfo() throws IOException {

    ItBitAccountInfoReturn[] info = itBitAuthenticated.getInfo(signatureCreator, new Date().getTime(), exchange.getNonceFactory(), userId);
    return info;
  }

  public String withdrawItBitFunds(String currency, BigDecimal amount, String address) throws IOException {

    String formattedAmount = ItBitAdapters.formatCryptoAmount(amount);

    ItBitWithdrawalRequest request = new ItBitWithdrawalRequest(currency, formattedAmount, address);
    ItBitWithdrawalResponse response = itBitAuthenticated.requestWithdrawal(signatureCreator, new Date().getTime(), exchange.getNonceFactory(), walletId, request);
    return response.getId();
  }

  public String requestItBitDepositAddress(String currency, String... args) throws IOException {

    Map<String, String> metadata = new HashMap<String, String>();
    for (int i = 0; i < args.length - 1; i += 2) {
      metadata.put(args[i], args[i+1]);
    }

    ItBitDepositRequest request = new ItBitDepositRequest(currency, metadata);
    ItBitDepositResponse response = itBitAuthenticated.requestDeposit(signatureCreator, new Date().getTime(), exchange.getNonceFactory(), walletId, request);
    return response.getDepositAddress();
  }

  public ItBitAccountInfoReturn getItBitAccountInfo(String walletId) throws IOException {

    ItBitAccountInfoReturn itBitAccountInfoReturn = itBitAuthenticated.getWallet(signatureCreator, new Date().getTime(), exchange.getNonceFactory(),
        walletId);
    return itBitAccountInfoReturn;
  }
}

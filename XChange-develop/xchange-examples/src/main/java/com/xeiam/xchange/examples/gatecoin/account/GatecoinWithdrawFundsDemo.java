package com.xeiam.xchange.examples.gatecoin.account;

import java.io.IOException;
import java.math.BigDecimal;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.currency.Currency;
import com.xeiam.xchange.examples.gatecoin.GatecoinDemoUtils;
import com.xeiam.xchange.gatecoin.dto.account.Results.GatecoinWithdrawResult;
import com.xeiam.xchange.gatecoin.service.polling.GatecoinAccountServiceRaw;
import com.xeiam.xchange.service.polling.account.PollingAccountService;

/**
 * @author sumedha
 */
public class GatecoinWithdrawFundsDemo {
  public static void main(String[] args) throws IOException {

    Exchange gatecoin = GatecoinDemoUtils.createExchange();
    PollingAccountService accountService = gatecoin.getPollingAccountService();

    generic(accountService);
    raw((GatecoinAccountServiceRaw) accountService);
  }

  private static void generic(PollingAccountService accountService) throws IOException {

    String result = accountService.withdrawFunds(Currency.BTC, BigDecimal.valueOf(0.1), "AddresssName");
    System.out.println("WithdrawResult: " + result);
  }

  private static void raw(GatecoinAccountServiceRaw accountService) throws IOException {

    // Get the account information
    GatecoinWithdrawResult gatecoinDepositAddressResult = accountService.withdrawGatecoinFunds("BTC", BigDecimal.valueOf(0.1), "BATMAN");
    System.out.println("GatecoinDepositAddess: " + gatecoinDepositAddressResult.getResponseStatus());
  }
}

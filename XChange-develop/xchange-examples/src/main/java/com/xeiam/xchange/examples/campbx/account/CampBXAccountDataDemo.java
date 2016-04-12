package com.xeiam.xchange.examples.campbx.account;

import java.io.IOException;
import java.math.BigDecimal;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.campbx.CampBXExchange;
import com.xeiam.xchange.currency.Currency;
import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.service.polling.account.PollingAccountService;

/**
 * Demonstrate requesting Market Data from CampBX
 */
public class CampBXAccountDataDemo {

  public static void main(String[] args) throws IOException {

    // Use the factory to get Campbx exchange API using default settings
    Exchange campbx = ExchangeFactory.INSTANCE.createExchange(CampBXExchange.class.getName());

    campbx.getExchangeSpecification().setUserName("XChange");
    campbx.getExchangeSpecification().setPassword("The Java API");

    PollingAccountService accountService = campbx.getPollingAccountService();

    AccountInfo wallet = accountService.getAccountInfo();
    System.out.println("wallet = " + wallet);

    String depositAddr = accountService.requestDepositAddress(Currency.BTC);
    System.out.println("depositAddr = " + depositAddr);

    String txid = accountService.withdrawFunds(Currency.BTC, new BigDecimal("0.1"), "1FgpMU9CgQffjLK5YoR2yK5XGj5cq4iCBf");
    System.out.println("See the withdrawal transaction: http://blockchain.info/tx-index/" + txid);
  }

}

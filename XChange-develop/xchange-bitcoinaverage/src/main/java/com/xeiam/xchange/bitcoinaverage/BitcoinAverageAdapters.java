package com.xeiam.xchange.bitcoinaverage;

import static com.xeiam.xchange.currency.Currency.BTC;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.xeiam.xchange.bitcoinaverage.dto.marketdata.BitcoinAverageTicker;
import com.xeiam.xchange.bitcoinaverage.dto.marketdata.BitcoinAverageTickers;
import com.xeiam.xchange.bitcoinaverage.dto.meta.BitcoinAverageMetaData;
import com.xeiam.xchange.currency.Currency;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.xeiam.xchange.dto.meta.CurrencyMetaData;
import com.xeiam.xchange.dto.meta.ExchangeMetaData;
import com.xeiam.xchange.dto.meta.MarketMetaData;

/**
 * Various adapters for converting from BitcoinAverage DTOs to XChange DTOs
 */
public final class BitcoinAverageAdapters {

  /**
   * private Constructor
   */
  private BitcoinAverageAdapters() {

  }

  /**
   * Adapts a BitcoinAverageTicker to a Ticker Object
   *
   * @param bitcoinAverageTicker
   * @return Ticker
   */
  public static Ticker adaptTicker(BitcoinAverageTicker bitcoinAverageTicker, CurrencyPair currencyPair) {

    BigDecimal last = bitcoinAverageTicker.getLast();
    BigDecimal bid = bitcoinAverageTicker.getBid();
    BigDecimal ask = bitcoinAverageTicker.getAsk();
    Date timestamp = bitcoinAverageTicker.getTimestamp();
    BigDecimal volume = bitcoinAverageTicker.getVolume();

    return new Ticker.Builder().currencyPair(currencyPair).last(last).bid(bid).ask(ask).volume(volume).timestamp(timestamp).build();
  }

  public static ExchangeMetaData adaptMetaData(BitcoinAverageTickers tickers, BitcoinAverageMetaData bAMetaData) {
    Map<CurrencyPair, MarketMetaData> currencyPairs = new HashMap<CurrencyPair, MarketMetaData>();
    for (String currency : tickers.getTickers().keySet())
      currencyPairs.put(new CurrencyPair(BTC, Currency.getInstance(currency)), new MarketMetaData(null, null, bAMetaData.priceScale));
    return new ExchangeMetaData(currencyPairs, Collections.<Currency, CurrencyMetaData> emptyMap(), null, null, null);
  }
}

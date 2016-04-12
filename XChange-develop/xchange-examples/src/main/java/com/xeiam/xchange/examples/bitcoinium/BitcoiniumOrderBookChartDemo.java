package com.xeiam.xchange.examples.bitcoinium;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.markers.SeriesMarkers;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.bitcoinium.BitcoiniumExchange;
import com.xeiam.xchange.bitcoinium.dto.marketdata.BitcoiniumOrderbook;
import com.xeiam.xchange.bitcoinium.dto.marketdata.BitcoiniumOrderbook.CondensedOrder;
import com.xeiam.xchange.bitcoinium.service.polling.BitcoiniumMarketDataServiceRaw;

/**
 * Demonstrates plotting an OrderBook with XChart
 *
 * @author timmolter
 */
public class BitcoiniumOrderBookChartDemo {

  public static void main(String[] args) throws Exception {

    ExchangeSpecification exchangeSpecification = new ExchangeSpecification(BitcoiniumExchange.class.getName());
    // exchangeSpecification.setPlainTextUri("http://openexchangerates.org");
    exchangeSpecification.setApiKey("42djci5kmbtyzrvglfdw3e2dgmh5mr37");
    System.out.println(exchangeSpecification.toString());
    Exchange bitcoiniumExchange = ExchangeFactory.INSTANCE.createExchange(exchangeSpecification);

    // Interested in the public polling market data feed (no authentication)
    BitcoiniumMarketDataServiceRaw bitcoiniumMarketDataService = (BitcoiniumMarketDataServiceRaw) bitcoiniumExchange.getPollingMarketDataService();

    System.out.println("fetching data...");

    // Get the latest order book data for BTC/USD - BITSTAMP
    BitcoiniumOrderbook bitcoiniumOrderbook = bitcoiniumMarketDataService.getBitcoiniumOrderbook("BTC", "BITSTAMP_USD", "TEN_PERCENT");

    System.out.println("Order book: " + bitcoiniumOrderbook);
    System.out.println("received data.");

    System.out.println("plotting...");

    // Create Chart
    XYChart chart = new XYChartBuilder().width(800).height(600).title("Bitcoinium Order Book - BITSTAMP_BTC_USD").xAxisTitle("BTC").yAxisTitle("USD")
        .build();

    // Customize Chart
    chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Area);

    // BIDS

    // Collections.reverse(bitcoiniumOrderbook.getBidPriceList());
    // Collections.reverse(bitcoiniumOrderbook.getBidVolumeList());

    // Bids Series
    List<Float> bidsPriceData = getPriceData(bitcoiniumOrderbook.getBids());
    Collections.reverse(bidsPriceData);
    List<Float> bidsVolumeData = getVolumeData(bitcoiniumOrderbook.getBids());
    Collections.reverse(bidsVolumeData);

    XYSeries series = chart.addSeries("bids", bidsPriceData, bidsVolumeData);
    series.setMarker(SeriesMarkers.NONE);

    // ASKS

    // Asks Series
    series = chart.addSeries("asks", getPriceData(bitcoiniumOrderbook.getAsks()), getVolumeData(bitcoiniumOrderbook.getAsks()));
    series.setMarker(SeriesMarkers.NONE);

    new SwingWrapper(chart).displayChart();
  }

  private static List<Float> getPriceData(CondensedOrder[] condensedOrders) {

    List<Float> priceData = new ArrayList<Float>();
    for (int i = 0; i < condensedOrders.length; i++) {
      priceData.add(condensedOrders[i].getPrice().floatValue());
    }
    return priceData;
  }

  private static List<Float> getVolumeData(CondensedOrder[] condensedOrders) {

    List<Float> volumeData = new ArrayList<Float>();
    for (int i = 0; i < condensedOrders.length; i++) {
      volumeData.add(condensedOrders[i].getVolume().floatValue());
    }
    return volumeData;
  }
}

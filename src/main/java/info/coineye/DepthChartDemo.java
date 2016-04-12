package info.coineye;


import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.bitfinex.v1.BitfinexExchange;
import com.xeiam.xchange.currency.Currency;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.service.polling.marketdata.PollingMarketDataService;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Demonstrate requesting OrderBook from Bitstamp and plotting it using XChart.
 */
public class DepthChartDemo {

    private static BigDecimal highScore = new BigDecimal("0");


    public static void main(String[] args) throws IOException {

        // Use the factory to get the version 1 Bitstamp exchange API using default settings
        Exchange bitfinexExchange = ExchangeFactory.INSTANCE.createExchange(BitfinexExchange.class.getName());


        for (int i = 0; i < 100; i++) {
            analyzeDepth(bitfinexExchange);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    }

    private static void analyzeDepth(Exchange bitfinexExchange) throws IOException {


        PollingMarketDataService marketDataService = bitfinexExchange.getPollingMarketDataService();


        OrderBook ordersBTCUSD = marketDataService.getOrderBook(new CurrencyPair(Currency.BTC, Currency.USD));
        OrderBook ordersETHBTC = marketDataService.getOrderBook(new CurrencyPair(Currency.ETH, Currency.BTC));
        OrderBook ordersETHUSD = marketDataService.getOrderBook(new CurrencyPair(Currency.ETH, Currency.USD));


        System.out.println("========================================================================================");

        Log.blue("BTC USD ------------------------");
        Log.red(ordersBTCUSD.getAsks().get(0).getLimitPrice());
        Log.green(ordersBTCUSD.getBids().get(0).getLimitPrice());

        Log.blue("ETH BTC ------------------------");
        Log.red(ordersETHBTC.getAsks().get(0).getLimitPrice());
        Log.green(ordersETHBTC.getBids().get(0).getLimitPrice());

        Log.blue("ETH USD ------------------------");
        Log.red(ordersETHUSD.getAsks().get(0).getLimitPrice());
        Log.green(ordersETHUSD.getBids().get(0).getLimitPrice());


        BigDecimal BTC_USD = ordersBTCUSD.getAsks().get(0).getLimitPrice();
        BigDecimal USD_BTC = new BigDecimal("1").divide(BTC_USD, 10, RoundingMode.HALF_UP);
        BigDecimal ETH_BTC = ordersETHBTC.getBids().get(0).getLimitPrice();
        BigDecimal BTC_ETH = new BigDecimal("1").divide(ETH_BTC, 10, RoundingMode.HALF_UP);
        BigDecimal ETH_USD = ordersETHUSD.getBids().get(0).getLimitPrice();
        BigDecimal USD_ETH = new BigDecimal("1").divide(ETH_USD, 10, RoundingMode.HALF_UP);

        Log.purple(BTC_USD);
        Log.purple(USD_BTC);
        Log.purple(ETH_BTC);
        Log.purple(BTC_ETH);
        Log.purple(ETH_USD);
        Log.purple(USD_ETH);

        BigDecimal startAmountUSD = new BigDecimal("100");
        Log.cyan(startAmountUSD);


        BigDecimal amountBTC = startAmountUSD.multiply(USD_BTC);
        Log.cyan(amountBTC);

        BigDecimal amountETH = amountBTC.multiply(BTC_ETH);
        Log.cyan(amountETH);


        BigDecimal endAmountUSD = amountETH.multiply(ETH_USD);
        Log.cyan(endAmountUSD);

        BigDecimal prof = endAmountUSD.subtract(startAmountUSD);

        BigDecimal profPercent = prof.divide(startAmountUSD, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));



        Log.cyan("profit: " + prof + "  ||  percent: " + profPercent + "%");


        if(profPercent.abs().compareTo(highScore) == 1)
            highScore = profPercent.abs();

        Log.cyan("***HIGH SCORE : " + highScore);






//
////
////        // Create Chart
////        Chart chart = new Chart(800, 500);
////
////        // Customize Chart
////        chart.setChartTitle("XXX");
////        chart.setYAxisTitle("ETH");
////        chart.setXAxisTitle("USD");
////        chart.getStyleManager().setChartType(StyleManager.ChartType.Area);
//
//        // BIDS
//        List<Number> xData = new ArrayList<Number>();
//        List<Number> yData = new ArrayList<Number>();
//        BigDecimal accumulatedBidUnits = new BigDecimal("0");
//        for (LimitOrder limitOrder : orderBook.getBids()) {
//            if (limitOrder.getLimitPrice().doubleValue() > 10) {
//                xData.add(limitOrder.getLimitPrice());
//                accumulatedBidUnits = accumulatedBidUnits.add(limitOrder.getTradableAmount());
//                yData.add(accumulatedBidUnits);
//            }
//        }
//        Collections.reverse(xData);
//        Collections.reverse(yData);
//
//        // Bids Series
////        Series series = chart.addSeries("bids", xData, yData);
////        series.setMarker(SeriesMarker.NONE);
//
//        // ASKS
//        xData = new ArrayList<Number>();
//        yData = new ArrayList<Number>();
//        BigDecimal accumulatedAskUnits = new BigDecimal("0");
//        for (LimitOrder limitOrder : orderBook.getAsks()) {
//            if (limitOrder.getLimitPrice().doubleValue() < 1000) {
//                xData.add(limitOrder.getLimitPrice());
//                accumulatedAskUnits = accumulatedAskUnits.add(limitOrder.getTradableAmount());
//                yData.add(accumulatedAskUnits);
//            }
//        }
//
//        // Asks Series
////        series = chart.addSeries("asks", xData, yData);
////        series.setMarker(SeriesMarker.NONE);
//
////        new SwingWrapper(chart).displayChart();
    }

}

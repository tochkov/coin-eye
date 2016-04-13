import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.bitfinex.v1.BitfinexExchange;
import com.xeiam.xchange.currency.Currency;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.service.polling.marketdata.PollingMarketDataService;
import info.coineye.Log;
import info.coineye.TrollBox;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Demonstrate requesting OrderBook from Bitstamp and plotting it using XChart.
 */
public class ArbiHack {

    private static BigDecimal highScore1 = new BigDecimal("-100");
    private static BigDecimal highScore2 = new BigDecimal("-100");
    private static BigDecimal highScore3 = new BigDecimal("-100");
    private static BigDecimal highScore4 = new BigDecimal("-100");
    private static BigDecimal highScore5 = new BigDecimal("-100");
    private static BigDecimal highScore6 = new BigDecimal("-100");

    private static ArrayList<BigDecimal> listScores1 = new ArrayList<>();
    private static ArrayList<BigDecimal> listScores2 = new ArrayList<>();
    private static ArrayList<BigDecimal> listScores3 = new ArrayList<>();
    private static ArrayList<BigDecimal> listScores4 = new ArrayList<>();
    private static ArrayList<BigDecimal> listScores5 = new ArrayList<>();
    private static ArrayList<BigDecimal> listScores6 = new ArrayList<>();

    private static long sleepTime = 2000;

    public static void main(String[] args) throws IOException {

        // Use the factory to get the version 1 Bitstamp exchange API using default settings
        Exchange bitfinexExchange = ExchangeFactory.INSTANCE.createExchange(BitfinexExchange.class.getName());


        while (true) {

            analyzeDepth(bitfinexExchange);

            try {
                Thread.sleep(sleepTime);
                sleepTime = 2000;
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

        // BTC_USD --> how much 1 BTC gets you in USD (amount of USD that 1 BTC can buy)
        BigDecimal BTC_USD_ask = getAsk(ordersBTCUSD);
        BigDecimal BTC_USD_bid = getBid(ordersBTCUSD);

        // BTC_USD --> how much 1 USD gets you in BTC (amount of BTC that 1 USD can buy)
        BigDecimal USD_BTC_ask = reciprocal(BTC_USD_bid);
        BigDecimal USD_BTC_bid = reciprocal(BTC_USD_ask);

        BigDecimal ETH_BTC_ask = getAsk(ordersETHBTC);
        BigDecimal ETH_BTC_bid = getBid(ordersETHBTC);

        BigDecimal BTC_ETH_ask = reciprocal(ETH_BTC_bid);
        BigDecimal BTC_ETH_bid = reciprocal(ETH_BTC_ask);

        BigDecimal ETH_USD_ask = getAsk(ordersETHUSD);
        BigDecimal ETH_USD_bid = getBid(ordersETHUSD);

        BigDecimal USD_ETH_ask = reciprocal(ETH_USD_bid);
        BigDecimal USD_ETH_bid = reciprocal(ETH_USD_ask);

//        Log.cyan("......................");
//        Log.cyan("BTC_USD ask: " + BTC_USD_ask + " || bid: " + BTC_USD_bid);
//        Log.cyan("USD_BTC ask: " + USD_BTC_ask + " || bid: " + USD_BTC_bid);
//        Log.cyan("ETH_BTC ask: " + ETH_BTC_ask + " || bid: " + ETH_BTC_bid);
//        Log.cyan("BTC_ETH ask: " + BTC_ETH_ask + " || bid: " + BTC_ETH_bid);
//        Log.cyan("ETH_USD ask: " + ETH_USD_ask + " || bid: " + ETH_USD_bid);
//        Log.cyan("USD_ETH ask: " + USD_ETH_ask + " || bid: " + USD_ETH_bid);
//        Log.cyan("......................");


        BigDecimal advantage1 = getAdvantagePercent("USD-->BTC-->ETH :: INSTA", new BigDecimal("100"), USD_BTC_bid, BTC_ETH_bid, ETH_USD_bid);
        BigDecimal advantage2 = getAdvantagePercent("USD-->ETH-->BTC :: INSTA", new BigDecimal("100"), USD_ETH_bid, ETH_BTC_bid, BTC_USD_bid);

        BigDecimal advantage3 = getAdvantagePercent("BTC-->USD-->ETH :: INSTA", new BigDecimal("1"), BTC_USD_bid, USD_ETH_bid, ETH_BTC_bid);
        BigDecimal advantage4 = getAdvantagePercent("BTC-->ETH-->USD :: INSTA", new BigDecimal("1"), BTC_ETH_bid, ETH_USD_bid, USD_BTC_bid);

        BigDecimal advantage5 = getAdvantagePercent("ETH-->BTC-->USD :: INSTA", new BigDecimal("10"), ETH_BTC_bid, BTC_USD_bid, USD_ETH_bid);
        BigDecimal advantage6 = getAdvantagePercent("ETH-->USD-->BTC :: INSTA", new BigDecimal("10"), ETH_USD_bid, USD_BTC_bid, BTC_ETH_bid);


//        Log.cyan("......................");
//
//        BigDecimal advantage1 = getAdvantagePercent("USD-->BTC-->ETH ++ MAKER", new BigDecimal("100"), USD_BTC_ask, BTC_ETH_bid, ETH_USD_bid);
//        BigDecimal advantage2 = getAdvantagePercent("USD-->ETH-->BTC ++ MAKER", new BigDecimal("100"), USD_ETH_ask, ETH_BTC_bid, BTC_USD_bid);
//
//        BigDecimal advantage3 = getAdvantagePercent("BTC-->USD-->ETH ++ MAKER", new BigDecimal("1"), BTC_USD_ask, USD_ETH_bid, ETH_BTC_bid);
//        BigDecimal advantage4 = getAdvantagePercent("BTC-->ETH-->USD ++ MAKER", new BigDecimal("1"), BTC_ETH_ask, ETH_USD_bid, USD_BTC_bid);
//
//        BigDecimal advantage5 = getAdvantagePercent("ETH-->BTC-->USD ++ MAKER", new BigDecimal("10"), ETH_BTC_ask, BTC_USD_bid, USD_ETH_bid);
//        BigDecimal advantage6 = getAdvantagePercent("ETH-->USD-->BTC ++ MAKER", new BigDecimal("10"), ETH_USD_ask, USD_BTC_bid, BTC_ETH_bid);

        Log.cyan("......................");

        if (advantage1.compareTo(highScore1) == 1)
            highScore1 = advantage1;
        if (advantage2.compareTo(highScore2) == 1)
            highScore2 = advantage2;
        if (advantage3.compareTo(highScore3) == 1)
            highScore3 = advantage3;
        if (advantage4.compareTo(highScore4) == 1)
            highScore4 = advantage4;
        if (advantage5.compareTo(highScore5) == 1)
            highScore5 = advantage5;
        if (advantage6.compareTo(highScore6) == 1)
            highScore6 = advantage6;

        if (advantage1.compareTo(new BigDecimal("0.01")) == 1) {
            listScores1.add(advantage1);
            sleepTime = 15000;
        }
        if (advantage2.compareTo(new BigDecimal("0.01")) == 1) {
            listScores2.add(advantage2);
            sleepTime = 15000;
        }
        if (advantage3.compareTo(new BigDecimal("0.01")) == 1) {
            listScores3.add(advantage3);
            sleepTime = 15000;
        }
        if (advantage4.compareTo(new BigDecimal("0.01")) == 1) {
            listScores4.add(advantage4);
            sleepTime = 15000;
        }
        if (advantage5.compareTo(new BigDecimal("0.01")) == 1) {
            listScores5.add(advantage5);
            sleepTime = 15000;
        }
        if (advantage6.compareTo(new BigDecimal("0.01")) == 1) {
            listScores6.add(advantage6);
            sleepTime = 15000;
        }

        Log.cyan("***HIGH SCORE 1: " + highScore1 + " ||||| " + listScores1);
        Log.cyan("***HIGH SCORE 2: " + highScore2 + " ||||| " + listScores2);
        Log.cyan("***HIGH SCORE 3: " + highScore3 + " ||||| " + listScores3);
        Log.cyan("***HIGH SCORE 4: " + highScore4 + " ||||| " + listScores4);
        Log.cyan("***HIGH SCORE 5: " + highScore5 + " ||||| " + listScores5);
        Log.cyan("***HIGH SCORE 6: " + highScore6 + " ||||| " + listScores6);

    }

    private static BigDecimal TAKER_FEE_APPLIED = new BigDecimal("1").subtract(new BigDecimal("0.002"));
    private static BigDecimal MAKER_FEE_APPLIED = new BigDecimal("1").subtract(new BigDecimal("0.001"));

    private static BigDecimal getAdvantagePercent(String label, BigDecimal startAmount, BigDecimal firstPairPrice, BigDecimal secondPairPrice, BigDecimal thirdPairPrice) {

        BigDecimal firstPairAmount = startAmount.multiply(TAKER_FEE_APPLIED).multiply(firstPairPrice);
        BigDecimal secondPairAmount = firstPairAmount.multiply(TAKER_FEE_APPLIED).multiply(secondPairPrice);
        BigDecimal finalAmount = secondPairAmount.multiply(TAKER_FEE_APPLIED).multiply(thirdPairPrice);

        BigDecimal diff = finalAmount.subtract(startAmount);

        BigDecimal advantagePercent = diff.divide(startAmount, 10, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));

        Log.cyan(label + " : " + advantagePercent + "%");
//        Log.cyan("final : " + finalAmount);
        return advantagePercent;
    }

    private static BigDecimal getAdvantagePercentMakerStart(String label, BigDecimal startAmount, BigDecimal firstPairPrice, BigDecimal secondPairPrice, BigDecimal thirdPairPrice) {

        BigDecimal firstPairAmount = startAmount.multiply(MAKER_FEE_APPLIED).multiply(firstPairPrice);
        BigDecimal secondPairAmount = firstPairAmount.multiply(TAKER_FEE_APPLIED).multiply(secondPairPrice);
        BigDecimal finalAmount = secondPairAmount.multiply(TAKER_FEE_APPLIED).multiply(thirdPairPrice);

        BigDecimal diff = finalAmount.subtract(startAmount);

        BigDecimal advantagePercent = diff.divide(startAmount, 10, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));

        Log.cyan(label + " : " + advantagePercent + "%");
//        Log.cyan("final : " + finalAmount);
        return advantagePercent;
    }

    private static BigDecimal getAdvantagePercentSmallerFees(String label, BigDecimal startAmount, BigDecimal firstPairPrice, BigDecimal secondPairPrice, BigDecimal thirdPairPrice) {

        BigDecimal firstPairAmount = startAmount.multiply(MAKER_FEE_APPLIED).multiply(firstPairPrice);
        BigDecimal secondPairAmount = firstPairAmount.multiply(MAKER_FEE_APPLIED).multiply(secondPairPrice);
        BigDecimal finalAmount = secondPairAmount.multiply(MAKER_FEE_APPLIED).multiply(thirdPairPrice);

        BigDecimal diff = finalAmount.subtract(startAmount);

        BigDecimal advantagePercent = diff.divide(startAmount, 10, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));

        Log.cyan(label + " : " + advantagePercent + "%");
//        Log.cyan("final : " + finalAmount);
        return advantagePercent;
    }


    private static BigDecimal getAsk(OrderBook orderBook) {
        return orderBook.getAsks().get(0).getLimitPrice();
    }

    private static BigDecimal getBid(OrderBook orderBook) {
        return orderBook.getBids().get(0).getLimitPrice();
    }

    private static BigDecimal reciprocal(BigDecimal base) {
        return new BigDecimal("1").divide(base, 10, RoundingMode.HALF_UP);
    }

}

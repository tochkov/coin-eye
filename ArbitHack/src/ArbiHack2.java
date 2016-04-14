import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.bitfinex.v1.BitfinexExchange;
import com.xeiam.xchange.currency.Currency;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.service.polling.marketdata.PollingMarketDataService;
import info.coineye.Log;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * TODO
 * <p>
 * Make all numbers primitive
 * <p>
 * Refactor to arrays;
 */
public class ArbiHack2 {

    private static final String DESIRED_PERCENT = "0.0015";

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


        BigDecimal advantage1 = getEntrancePoint("USD-->BTC-->ETH :: INSTA", new BigDecimal("100"), new BigDecimal(DESIRED_PERCENT), BTC_ETH_bid, ETH_USD_bid);
        BigDecimal advantage3 = getEntrancePoint("BTC-->USD-->ETH :: INSTA", new BigDecimal("1"), new BigDecimal(DESIRED_PERCENT), USD_ETH_bid, ETH_BTC_bid);

        printDepth(ordersBTCUSD, "BTCUSD", reciprocal(advantage1), advantage3);
        Log.cyan(reciprocal(advantage1) + " | " + advantage3);
        Log.cyan("validation:");
        Log.cyan(getResultForTest(new BigDecimal("100"), advantage1, BTC_ETH_bid, ETH_USD_bid));
        Log.cyan(getResultForTest(new BigDecimal("1"), advantage3, USD_ETH_bid, ETH_BTC_bid));


        BigDecimal advantage2 = getEntrancePoint("USD-->ETH-->BTC :: INSTA", new BigDecimal("100"), new BigDecimal(DESIRED_PERCENT), ETH_BTC_bid, BTC_USD_bid);
        BigDecimal advantage6 = getEntrancePoint("ETH-->USD-->BTC :: INSTA", new BigDecimal("10"), new BigDecimal(DESIRED_PERCENT), USD_BTC_bid, BTC_ETH_bid);

        printDepth(ordersETHUSD, "ETHUSD", reciprocal(advantage2), advantage6);
        Log.cyan(reciprocal(advantage2) + " | " + advantage6);
        Log.cyan("validation:");
        Log.cyan(getResultForTest(new BigDecimal("100"), advantage2, ETH_BTC_bid, BTC_USD_bid));
        Log.cyan(getResultForTest(new BigDecimal("10"), advantage6, USD_BTC_bid, BTC_ETH_bid));


        BigDecimal advantage4 = getEntrancePoint("BTC-->ETH-->USD :: INSTA", new BigDecimal("1"), new BigDecimal(DESIRED_PERCENT), ETH_USD_bid, USD_BTC_bid);
        BigDecimal advantage5 = getEntrancePoint("ETH-->BTC-->USD :: INSTA", new BigDecimal("10"), new BigDecimal(DESIRED_PERCENT), BTC_USD_bid, USD_ETH_bid);

        printDepth(ordersETHBTC, "ETHBTC", reciprocal(advantage4), advantage5);
        Log.cyan(reciprocal(advantage4) + " | " + advantage5);
        Log.cyan("validation:");
        Log.cyan(getResultForTest(new BigDecimal("1"), advantage4, ETH_USD_bid, USD_BTC_bid));
        Log.cyan(getResultForTest(new BigDecimal("10"), advantage5, BTC_USD_bid, USD_ETH_bid));


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

    private static BigDecimal getEntrancePoint(String label, BigDecimal startAmount, BigDecimal desiredPercent, BigDecimal secondPairPrice, BigDecimal thirdPairPrice) {

        BigDecimal endAmount = startAmount.multiply(new BigDecimal("1").add(desiredPercent));
//        Log.blue("endAmount " + endAmount);

        BigDecimal secondAmount = endAmount.divide(thirdPairPrice.multiply(TAKER_FEE_APPLIED), 10, RoundingMode.HALF_UP);
//        Log.blue("secondAmount " + secondAmount);

        BigDecimal firsAmount = secondAmount.divide(secondPairPrice.multiply(TAKER_FEE_APPLIED), 10, RoundingMode.HALF_UP);
//        Log.blue("firsAmount " + firsAmount);

        BigDecimal targetPrice = firsAmount.divide(startAmount.multiply(MAKER_FEE_APPLIED), 10, RoundingMode.HALF_UP);

//        Log.cyan(label + " : " + targetPrice);


        return targetPrice;
    }

    private static BigDecimal getResultForTest(BigDecimal startAmount, BigDecimal firstPairPrice, BigDecimal secondPairPrice, BigDecimal thirdPairPrice) {

        BigDecimal firstPairAmount = startAmount.multiply(MAKER_FEE_APPLIED).multiply(firstPairPrice);
        BigDecimal secondPairAmount = firstPairAmount.multiply(TAKER_FEE_APPLIED).multiply(secondPairPrice);
        BigDecimal finalAmount = secondPairAmount.multiply(TAKER_FEE_APPLIED).multiply(thirdPairPrice);

        BigDecimal diff = finalAmount.subtract(startAmount);

        BigDecimal advantagePercent = diff.divide(startAmount, 10, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));

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

    private static void printDepth(OrderBook orderBook, String label, BigDecimal entranceBid, BigDecimal entranceAsk) {


        Log.cyan("----------------------- " + label + "--------------------------------");

        ArrayList<LimitOrder> asks = new ArrayList<>(orderBook.getAsks());
        ArrayList<LimitOrder> bids = new ArrayList<>(orderBook.getBids());

        BigDecimal totalBid = new BigDecimal("0");
        BigDecimal totalAsk = new BigDecimal("0");

        boolean foundEntranceBid = false;
        boolean foundEntranceAsk = false;

        for (int i = 0; i < 15; i++) {

            BigDecimal bidPrice = bids.get(i).getLimitPrice().round(new MathContext(10, RoundingMode.HALF_UP));
            BigDecimal bidVolume = bids.get(i).getTradableAmount().round(new MathContext(0, RoundingMode.HALF_UP));

            totalBid.add(bidVolume);

            boolean enterBid = false;

            if (!foundEntranceBid && entranceBid.compareTo(bidPrice) == 1) {
                foundEntranceBid = true;
                enterBid = true;
            }

//                enterBid = entranceBid.compareTo(bidPrice) == -1;

            Log.bid(bidPrice + " | " + bidVolume.toBigInteger(), enterBid);

            BigDecimal askPrice = asks.get(i).getLimitPrice().round(new MathContext(5));
            BigDecimal askVolume = asks.get(i).getTradableAmount();

            totalAsk.add(askVolume);

            boolean enterAsk = false;

            if (!foundEntranceAsk && entranceAsk.compareTo(askPrice) == -1) {
                foundEntranceAsk = true;
                enterAsk = true;
            }

//                enterAsk = entranceAsk.compareTo(bidPrice) == -1;

            Log.ask(askPrice + " | " + askVolume.toBigInteger(), enterAsk);
        }


    }

}

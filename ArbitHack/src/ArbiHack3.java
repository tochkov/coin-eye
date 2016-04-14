import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.bitfinex.v1.BitfinexExchange;
import com.xeiam.xchange.currency.Currency;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.service.polling.marketdata.PollingMarketDataService;
import com.xeiam.xchange.service.streaming.WebSocketEventProducer;
import info.coineye.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 * TODO
 * <p>
 * Make all numbers primitive
 * <p>
 * Refactor to arrays;
 */
public class ArbiHack3 {

    private static final double DESIRED_PERCENT = nominalPercent(0.1);

    public static final double STARTING_USD = 100;
    public static final double STARTING_BTC = 1;
    public static final double STARTING_ETH = 10;

    private static double TAKER_FEE = 0.2;
    private static double MAKER_FEE = 0.1;

    private static double APPLY_TAKER_FEE = 1 - nominalPercent(TAKER_FEE);
    private static double APPLY_MAKER_FEE = 1 - nominalPercent(MAKER_FEE);

    private static long sleepTime = 2000;

    public static void main(String[] args) throws IOException {

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
        double BTC_USD_ask = getAsk(ordersBTCUSD);
        double BTC_USD_bid = getBid(ordersBTCUSD);

        // BTC_USD --> how much 1 USD gets you in BTC (amount of BTC that 1 USD can buy)
        double USD_BTC_ask = reciprocal(BTC_USD_bid);
        double USD_BTC_bid = reciprocal(BTC_USD_ask);

        double ETH_BTC_ask = getAsk(ordersETHBTC);
        double ETH_BTC_bid = getBid(ordersETHBTC);

        double BTC_ETH_ask = reciprocal(ETH_BTC_bid);
        double BTC_ETH_bid = reciprocal(ETH_BTC_ask);

        double ETH_USD_ask = getAsk(ordersETHUSD);
        double ETH_USD_bid = getBid(ordersETHUSD);

        double USD_ETH_ask = reciprocal(ETH_USD_bid);
        double USD_ETH_bid = reciprocal(ETH_USD_ask);

//        Log.cyan("......................");
//        Log.cyan("BTC_USD ask: " + BTC_USD_ask + " || bid: " + BTC_USD_bid);
//        Log.cyan("USD_BTC ask: " + USD_BTC_ask + " || bid: " + USD_BTC_bid);
//        Log.cyan("ETH_BTC ask: " + ETH_BTC_ask + " || bid: " + ETH_BTC_bid);
//        Log.cyan("BTC_ETH ask: " + BTC_ETH_ask + " || bid: " + BTC_ETH_bid);
//        Log.cyan("ETH_USD ask: " + ETH_USD_ask + " || bid: " + ETH_USD_bid);
//        Log.cyan("USD_ETH ask: " + USD_ETH_ask + " || bid: " + USD_ETH_bid);
//        Log.cyan("......................");


        double advantage1 = getEntrancePoint(STARTING_USD, BTC_ETH_bid, ETH_USD_bid); //USD-->BTC-->ETH
        double advantage3 = getEntrancePoint(STARTING_BTC, USD_ETH_bid, ETH_BTC_bid); //BTC-->USD-->ETH

        printDepth(ordersBTCUSD, "BTCUSD", reciprocal(advantage1), advantage3);
        Log.cyan(reciprocal(advantage1) + " | " + advantage3);
        Log.cyan("validation:");
        Log.cyan(validateAdvantage(STARTING_USD, advantage1, BTC_ETH_bid, ETH_USD_bid));
        Log.cyan(validateAdvantage(STARTING_BTC, advantage3, USD_ETH_bid, ETH_BTC_bid));


        double advantage2 = getEntrancePoint(STARTING_USD, ETH_BTC_bid, BTC_USD_bid); //USD-->ETH-->BTC
        double advantage6 = getEntrancePoint(STARTING_ETH, USD_BTC_bid, BTC_ETH_bid); //ETH-->USD-->BTC

        printDepth(ordersETHUSD, "ETHUSD", reciprocal(advantage2), advantage6);
        Log.cyan(reciprocal(advantage2) + " | " + advantage6);
        Log.cyan("validation:");
        Log.cyan(validateAdvantage(STARTING_USD, advantage2, ETH_BTC_bid, BTC_USD_bid));
        Log.cyan(validateAdvantage(STARTING_ETH, advantage6, USD_BTC_bid, BTC_ETH_bid));


        double advantage4 = getEntrancePoint(STARTING_BTC, ETH_USD_bid, USD_BTC_bid); //BTC-->ETH-->USD
        double advantage5 = getEntrancePoint(STARTING_ETH, BTC_USD_bid, USD_ETH_bid); //ETH-->BTC-->USD

        printDepth(ordersETHBTC, "ETHBTC", reciprocal(advantage4), advantage5);
        Log.cyan(reciprocal(advantage4) + " | " + advantage5);
        Log.cyan("validation:");
        Log.cyan(validateAdvantage(STARTING_BTC, advantage4, ETH_USD_bid, USD_BTC_bid));
        Log.cyan(validateAdvantage(STARTING_ETH, advantage5, BTC_USD_bid, USD_ETH_bid));


    }

    private static double getEntrancePoint(double startAmount, double secondPairPrice, double thirdPairPrice) {

        double endAmount = startAmount * (1 + DESIRED_PERCENT);
//        Log.blue("endAmount " + endAmount);

        double secondAmount = endAmount / (thirdPairPrice * APPLY_TAKER_FEE);
//        Log.blue("secondAmount " + secondAmount);

        double firsAmount = secondAmount / (secondPairPrice * APPLY_TAKER_FEE);
//        Log.blue("firsAmount " + firsAmount);

        double targetPrice = firsAmount / (startAmount * APPLY_MAKER_FEE);


        return targetPrice;
    }

    private static double validateAdvantage(double startAmount, double firstPairPrice, double secondPairPrice, double thirdPairPrice) {

        double firstPairAmount = startAmount * APPLY_MAKER_FEE * firstPairPrice;
        double secondPairAmount = firstPairAmount * APPLY_TAKER_FEE * secondPairPrice;
        double finalAmount = secondPairAmount * APPLY_TAKER_FEE * thirdPairPrice;

        double diff = finalAmount - startAmount;

        double advantagePercent = diff / startAmount * 100;

        return advantagePercent;
    }


    private static double getAsk(OrderBook orderBook) {
        return orderBook.getAsks().get(0).getLimitPrice().doubleValue();
    }

    private static double getBid(OrderBook orderBook) {
        return orderBook.getBids().get(0).getLimitPrice().doubleValue();
    }

    private static double reciprocal(double base) {
        return 1 / base;
    }


    private static double nominalPercent(double percent) {
        return percent / 100;
    }


    private static void printDepth(OrderBook orderBook, String label, double entranceBid, double entranceAsk) {


        Log.cyan("----------------------- " + label + "--------------------------------");

        ArrayList<LimitOrder> asks = new ArrayList<>(orderBook.getAsks());
        ArrayList<LimitOrder> bids = new ArrayList<>(orderBook.getBids());

        double totalBid = 0;
        double totalAsk = 0;

        boolean foundEntranceBid = false;
        boolean foundEntranceAsk = false;

        for (int i = 0; i < 15; i++) {

            double bidPrice = bids.get(i).getLimitPrice().doubleValue();
            double bidVolume = bids.get(i).getTradableAmount().doubleValue();

            totalBid += bidVolume;

            boolean enterBid = false;

            if (!foundEntranceBid && entranceBid >= bidPrice) {
                foundEntranceBid = true;
                enterBid = true;
            }

            Log.bid(bidPrice + " | " + bidVolume + " | " + totalBid, enterBid);

            double askPrice = asks.get(i).getLimitPrice().doubleValue();
            double askVolume = asks.get(i).getTradableAmount().doubleValue();

            totalAsk += askVolume;

            boolean enterAsk = false;

            if (!foundEntranceAsk && entranceAsk <= askPrice) {
                foundEntranceAsk = true;
                enterAsk = true;
            }

            Log.ask(askPrice + " | " + askVolume + " | " + totalAsk, enterAsk);
        }


    }

}

package com.xeiam.xchange.poloniex.service.polling;

/**
 * @author Zach Holmes
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.Order;
import com.xeiam.xchange.dto.Order.OrderType;
import com.xeiam.xchange.dto.marketdata.Trades.TradeSortType;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.dto.trade.MarketOrder;
import com.xeiam.xchange.dto.trade.OpenOrders;
import com.xeiam.xchange.dto.trade.UserTrade;
import com.xeiam.xchange.dto.trade.UserTrades;
import com.xeiam.xchange.exceptions.ExchangeException;
import com.xeiam.xchange.exceptions.NotAvailableFromExchangeException;
import com.xeiam.xchange.exceptions.NotYetImplementedForExchangeException;
import com.xeiam.xchange.poloniex.PoloniexAdapters;
import com.xeiam.xchange.poloniex.PoloniexUtils;
import com.xeiam.xchange.poloniex.dto.trade.PoloniexOpenOrder;
import com.xeiam.xchange.poloniex.dto.trade.PoloniexUserTrade;
import com.xeiam.xchange.service.polling.trade.PollingTradeService;
import com.xeiam.xchange.service.polling.trade.params.TradeHistoryParamCurrencyPair;
import com.xeiam.xchange.service.polling.trade.params.TradeHistoryParams;
import com.xeiam.xchange.service.polling.trade.params.TradeHistoryParamsAll;
import com.xeiam.xchange.service.polling.trade.params.TradeHistoryParamsTimeSpan;
import com.xeiam.xchange.utils.DateUtils;

public class PoloniexTradeService extends PoloniexTradeServiceRaw implements PollingTradeService {

    /**
     * Constructor
     *
     * @param exchange
     */
    public PoloniexTradeService(Exchange exchange) {

        super(exchange);
    }

    @Override
    public OpenOrders getOpenOrders() throws IOException {

        HashMap<String, PoloniexOpenOrder[]> poloniexOpenOrders = returnOpenOrders();
        return PoloniexAdapters.adaptPoloniexOpenOrders(poloniexOpenOrders);
    }

    @Override
    public String placeMarketOrder(MarketOrder marketOrder) throws IOException {

        throw new NotAvailableFromExchangeException();
    }

    @Override
    public String placeLimitOrder(LimitOrder limitOrder) throws IOException {

        if (limitOrder.getType() == OrderType.BID) {
            return buy(limitOrder).getOrderNumber().toString();
        } else {
            return sell(limitOrder).getOrderNumber().toString();
        }
    }

    @Override
    public boolean cancelOrder(String orderId) throws IOException {

        return cancel(orderId);
    }

    /**
     * @param params Can optionally implement {@link TradeHistoryParamCurrencyPair} and {@link TradeHistoryParamsTimeSpan}. All other TradeHistoryParams
     *        types will be ignored.
     */
    @Override
    public UserTrades getTradeHistory(TradeHistoryParams params) throws IOException {

        CurrencyPair currencyPair = null;
        Date startTime = null;
        Date endTime = null;

        if (params instanceof TradeHistoryParamCurrencyPair) {
            currencyPair = ((TradeHistoryParamCurrencyPair) params).getCurrencyPair();
        }
        if (params instanceof TradeHistoryParamsTimeSpan) {
            startTime = ((TradeHistoryParamsTimeSpan) params).getStartTime();
            endTime = ((TradeHistoryParamsTimeSpan) params).getEndTime();
        }
        return getTradeHistory(currencyPair, DateUtils.toUnixTimeNullSafe(startTime), DateUtils.toUnixTimeNullSafe(endTime));
    }

    private UserTrades getTradeHistory(CurrencyPair currencyPair, final Long startTime, final Long endTime) throws IOException {

        List<UserTrade> trades = new ArrayList<UserTrade>();
        if (currencyPair == null) {
            HashMap<String, PoloniexUserTrade[]> poloniexUserTrades = returnTradeHistory(startTime, endTime);
            for (Map.Entry<String, PoloniexUserTrade[]> mapEntry : poloniexUserTrades.entrySet()) {
                currencyPair = PoloniexUtils.toCurrencyPair(mapEntry.getKey());
                for (PoloniexUserTrade poloniexUserTrade : mapEntry.getValue()) {
                    trades.add(PoloniexAdapters.adaptPoloniexUserTrade(poloniexUserTrade, currencyPair));
                }
            }
        } else {
            PoloniexUserTrade[] poloniexUserTrades = returnTradeHistory(currencyPair, startTime, endTime);
            for (PoloniexUserTrade poloniexUserTrade : poloniexUserTrades) {
                trades.add(PoloniexAdapters.adaptPoloniexUserTrade(poloniexUserTrade, currencyPair));
            }
        }

        return new UserTrades(trades, TradeSortType.SortByTimestamp);
    }

    /**
     * Create {@link TradeHistoryParams} that supports {@link TradeHistoryParamsTimeSpan} and {@link TradeHistoryParamCurrencyPair}.
     */

    @Override
    public TradeHistoryParams createTradeHistoryParams() {

        return new PoloniexTradeHistoryParams();
    }

    @Override
    public Collection<Order> getOrder(String... orderIds) throws ExchangeException, NotAvailableFromExchangeException, NotYetImplementedForExchangeException,
            IOException {
        throw new NotYetImplementedForExchangeException();
    }

    public static class PoloniexTradeHistoryParams implements TradeHistoryParamCurrencyPair, TradeHistoryParamsTimeSpan {

        private final TradeHistoryParamsAll all = new TradeHistoryParamsAll();

        @Override
        public void setCurrencyPair(CurrencyPair value) {

            all.setCurrencyPair(value);
        }

        @Override
        public CurrencyPair getCurrencyPair() {

            return all.getCurrencyPair();
        }

        @Override
        public void setStartTime(Date value) {

            all.setStartTime(value);
        }

        @Override
        public Date getStartTime() {

            return all.getStartTime();
        }

        @Override
        public void setEndTime(Date value) {

            all.setEndTime(value);
        }

        @Override
        public Date getEndTime() {

            return all.getEndTime();
        }
    }

}

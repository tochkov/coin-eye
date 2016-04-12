package com.xeiam.xchange.mercadobitcoin.service.polling;

import static com.xeiam.xchange.utils.DateUtils.toUnixTimeNullSafe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.currency.Currency;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.Order;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.dto.trade.MarketOrder;
import com.xeiam.xchange.dto.trade.OpenOrders;
import com.xeiam.xchange.dto.trade.UserTrades;
import com.xeiam.xchange.exceptions.ExchangeException;
import com.xeiam.xchange.exceptions.NotAvailableFromExchangeException;
import com.xeiam.xchange.exceptions.NotYetImplementedForExchangeException;
import com.xeiam.xchange.mercadobitcoin.MercadoBitcoinAdapters;
import com.xeiam.xchange.mercadobitcoin.MercadoBitcoinUtils;
import com.xeiam.xchange.mercadobitcoin.dto.MercadoBitcoinBaseTradeApiResult;
import com.xeiam.xchange.mercadobitcoin.dto.trade.MercadoBitcoinPlaceLimitOrderResult;
import com.xeiam.xchange.mercadobitcoin.dto.trade.MercadoBitcoinUserOrders;
import com.xeiam.xchange.service.polling.trade.PollingTradeService;
import com.xeiam.xchange.service.polling.trade.params.DefaultTradeHistoryParamCurrencyPair;
import com.xeiam.xchange.service.polling.trade.params.TradeHistoryParamCurrencyPair;
import com.xeiam.xchange.service.polling.trade.params.TradeHistoryParams;
import com.xeiam.xchange.service.polling.trade.params.TradeHistoryParamsIdSpan;
import com.xeiam.xchange.service.polling.trade.params.TradeHistoryParamsTimeSpan;

/**
 * @author Felipe Micaroni Lalli
 */
public class MercadoBitcoinTradeService extends MercadoBitcoinTradeServiceRaw implements PollingTradeService {

    /**
     * Constructor
     *
     * @param exchange
     */
    public MercadoBitcoinTradeService(Exchange exchange) {

        super(exchange);
    }

    @Override
    public OpenOrders getOpenOrders() throws IOException {

        MercadoBitcoinBaseTradeApiResult<MercadoBitcoinUserOrders> openOrdersBitcoinResult = getMercadoBitcoinUserOrders("btc_brl", null, "active", null, null,
                null, null);
        MercadoBitcoinBaseTradeApiResult<MercadoBitcoinUserOrders> openOrdersLitecoinResult = getMercadoBitcoinUserOrders("ltc_brl", null, "active", null,
                null, null, null);

        List<LimitOrder> limitOrders = new ArrayList<LimitOrder>();

        limitOrders.addAll(MercadoBitcoinAdapters.adaptOrders(CurrencyPair.BTC_BRL, openOrdersBitcoinResult));
        limitOrders.addAll(MercadoBitcoinAdapters.adaptOrders(new CurrencyPair(Currency.LTC, Currency.BRL), openOrdersLitecoinResult));

        return new OpenOrders(limitOrders);
    }

    @Override
    public Collection<Order> getOrder(String... orderIds) throws ExchangeException, NotAvailableFromExchangeException, NotYetImplementedForExchangeException,
            IOException {
        throw new NotYetImplementedForExchangeException();
    }

    @Override
    public String placeMarketOrder(MarketOrder marketOrder) throws IOException {

        throw new NotAvailableFromExchangeException();
    }

    /**
     * The result is not the pure order id. It is a composition with the currency pair and the order id (the same format used as parameter of
     * {@link #cancelOrder}). Please see {@link com.xeiam.xchange.mercadobitcoin.MercadoBitcoinUtils#makeMercadoBitcoinOrderId} .
     */
    @Override
    public String placeLimitOrder(LimitOrder limitOrder) throws IOException {

        String pair;

        if (limitOrder.getCurrencyPair().equals(CurrencyPair.BTC_BRL)) {
            pair = "btc_brl";
        } else if (limitOrder.getCurrencyPair().equals(new CurrencyPair(Currency.LTC, Currency.BRL))) {
            pair = "ltc_brl";
        } else {
            throw new NotAvailableFromExchangeException();
        }

        String type;

        if (limitOrder.getType() == Order.OrderType.BID) {
            type = "buy";
        } else {
            type = "sell";
        }

        MercadoBitcoinBaseTradeApiResult<MercadoBitcoinPlaceLimitOrderResult> newOrderResult = mercadoBitcoinPlaceLimitOrder(pair, type,
                limitOrder.getTradableAmount(), limitOrder.getLimitPrice());

        return MercadoBitcoinUtils.makeMercadoBitcoinOrderId(limitOrder.getCurrencyPair(), newOrderResult.getTheReturn().keySet().iterator().next());
    }

    /**
     * The ID is composed by the currency pair and the id number separated by colon, like: <code>btc_brl:3498</code> Please see and use
     * {@link com.xeiam.xchange.mercadobitcoin.MercadoBitcoinUtils#makeMercadoBitcoinOrderId} .
     */
    @Override
    public boolean cancelOrder(String orderId) throws IOException {

        String[] pairAndId = orderId.split(":");

        mercadoBitcoinCancelOrder(pairAndId[0], pairAndId[1]);

        return true;
    }

    /**
     * @param params Required parameter types: {@link TradeHistoryParamCurrencyPair}. Supported types: {@link TradeHistoryParamsIdSpan},
     * {@link TradeHistoryParamsTimeSpan}.
     *
     */
    @Override
    public UserTrades getTradeHistory(TradeHistoryParams params) throws IOException {
        CurrencyPair pair = ((TradeHistoryParamCurrencyPair) params).getCurrencyPair();

        String fromId = null;
        String toId = null;
        if (params instanceof TradeHistoryParamsIdSpan) {
            TradeHistoryParamsIdSpan paramsIdSpan = (TradeHistoryParamsIdSpan) params;
            fromId = paramsIdSpan.getStartId();
            toId = paramsIdSpan.getEndId();
        }

        Long fromDate = null;
        Long toDate = null;
        if (params instanceof TradeHistoryParamsTimeSpan) {
            TradeHistoryParamsTimeSpan paramsTimeSpan = (TradeHistoryParamsTimeSpan) params;
            fromDate = toUnixTimeNullSafe(paramsTimeSpan.getStartTime());
            toDate = toUnixTimeNullSafe(paramsTimeSpan.getEndTime());
        }

        MercadoBitcoinBaseTradeApiResult<MercadoBitcoinUserOrders> orders = getMercadoBitcoinUserOrders(MercadoBitcoinAdapters.adaptCurrencyPair(pair), null, /*all*/
                null, fromId, toId, fromDate, toDate);

        return MercadoBitcoinAdapters.toUserTrades(pair, orders);
    }

    @Override
    public TradeHistoryParams createTradeHistoryParams() {
        return new MercadoTradeHistoryParams(CurrencyPair.BTC_BRL);
    }

    public static class MercadoTradeHistoryParams extends DefaultTradeHistoryParamCurrencyPair implements TradeHistoryParamsIdSpan, TradeHistoryParamsTimeSpan {
        private String startId;
        private String endId;
        private Date startTime;
        private Date endTime;

        public MercadoTradeHistoryParams(CurrencyPair pair) {
            super(pair);
        }

        @Override
        public void setStartId(String startId) {
            this.startId = startId;
        }

        @Override
        public String getStartId() {
            return startId;
        }

        @Override
        public void setEndId(String endId) {
            this.endId = endId;
        }

        @Override
        public String getEndId() {
            return endId;
        }

        @Override
        public void setStartTime(Date startTime) {
            this.startTime = startTime;
        }

        @Override
        public Date getStartTime() {
            return startTime;
        }

        @Override
        public void setEndTime(Date endTime) {
            this.endTime = endTime;
        }

        @Override
        public Date getEndTime() {
            return endTime;
        }
    }

}

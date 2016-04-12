package com.xeiam.xchange.bitmarket;

import java.io.IOException;
import java.math.BigDecimal;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.xeiam.xchange.bitmarket.dto.account.BitMarketAccountInfoResponse;
import com.xeiam.xchange.bitmarket.dto.account.BitMarketDepositResponse;
import com.xeiam.xchange.bitmarket.dto.account.BitMarketWithdrawResponse;
import com.xeiam.xchange.bitmarket.dto.trade.BitMarketCancelResponse;
import com.xeiam.xchange.bitmarket.dto.trade.BitMarketHistoryOperationsResponse;
import com.xeiam.xchange.bitmarket.dto.trade.BitMarketHistoryTradesResponse;
import com.xeiam.xchange.bitmarket.dto.trade.BitMarketOrdersResponse;
import com.xeiam.xchange.bitmarket.dto.trade.BitMarketTradeResponse;

import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.SynchronizedValueFactory;

/**
 * @author kfonal
 */
@Path("api2/")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_JSON)
public interface BitMarketAuthenticated {

  @POST
  @FormParam("method")
  public BitMarketAccountInfoResponse info(@HeaderParam("API-Key") String apiKey, @HeaderParam("API-Hash") ParamsDigest sign,
      @FormParam("tonce") SynchronizedValueFactory<Long> timestamp) throws IOException;

  @POST
  @FormParam("method")
  public BitMarketWithdrawResponse withdraw(@HeaderParam("API-Key") String apiKey, @HeaderParam("API-Hash") ParamsDigest sign,
      @FormParam("tonce") SynchronizedValueFactory<Long> timestamp, @FormParam("currency") String currency, @FormParam("amount") BigDecimal amount,
      @FormParam("address") String address) throws IOException;

  @POST
  @FormParam("method")
  public BitMarketDepositResponse deposit(@HeaderParam("API-Key") String apiKey, @HeaderParam("API-Hash") ParamsDigest sign,
      @FormParam("tonce") SynchronizedValueFactory<Long> timestamp, @FormParam("currency") String currency) throws IOException;

  @POST
  @FormParam("method")
  public BitMarketOrdersResponse orders(@HeaderParam("API-Key") String apiKey, @HeaderParam("API-Hash") ParamsDigest sign,
      @FormParam("tonce") SynchronizedValueFactory<Long> timestamp) throws IOException;

  @POST
  @FormParam("method")
  public BitMarketTradeResponse trade(@HeaderParam("API-Key") String apiKey, @HeaderParam("API-Hash") ParamsDigest sign,
      @FormParam("tonce") SynchronizedValueFactory<Long> timestamp, @FormParam("market") String market, @FormParam("type") String type,
      @FormParam("amount") BigDecimal amount, @FormParam("rate") BigDecimal rate) throws IOException;

  @POST
  @FormParam("method")
  public BitMarketCancelResponse cancel(@HeaderParam("API-Key") String apiKey, @HeaderParam("API-Hash") ParamsDigest sign,
      @FormParam("tonce") SynchronizedValueFactory<Long> timestamp, @FormParam("id") long id) throws IOException;

  @POST
  @FormParam("method")
  public BitMarketHistoryTradesResponse trades(@HeaderParam("API-Key") String apiKey, @HeaderParam("API-Hash") ParamsDigest sign,
      @FormParam("tonce") SynchronizedValueFactory<Long> timestamp, @FormParam("market") String market, @FormParam("count") int count,
      @FormParam("start") long start) throws IOException;

  @POST
  @FormParam("method")
  public BitMarketHistoryOperationsResponse history(@HeaderParam("API-Key") String apiKey, @HeaderParam("API-Hash") ParamsDigest sign,
      @FormParam("tonce") SynchronizedValueFactory<Long> timestamp, @FormParam("currency") String currency, @FormParam("count") int count,
      @FormParam("start") long start) throws IOException;
}

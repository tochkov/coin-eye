package com.xeiam.xchange.bitcurex;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.xeiam.xchange.bitcurex.dto.marketdata.BitcurexDepth;
import com.xeiam.xchange.bitcurex.dto.marketdata.BitcurexTicker;
import com.xeiam.xchange.bitcurex.dto.marketdata.BitcurexTrade;

@Path("api")
@Produces(MediaType.APPLICATION_JSON)
public interface Bitcurex {

  @GET
  @Path("{currency}/ticker.json")
  public BitcurexTicker getTicker(@PathParam("currency") String currency) throws IOException;

  @GET
  @Path("{currency}/orderbook.json")
  public BitcurexDepth getFullDepth(@PathParam("currency") String currency) throws IOException;

  @GET
  @Path("{currency}/trades.json")
  public BitcurexTrade[] getTrades(@PathParam("currency") String currency) throws IOException;

}

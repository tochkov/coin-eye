package com.xeiam.xchange.bitkonan;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.xeiam.xchange.bitkonan.dto.marketdata.BitKonanOrderBook;
import com.xeiam.xchange.bitkonan.dto.marketdata.BitKonanTicker;

@Path("api")
@Produces(MediaType.APPLICATION_JSON)
public interface BitKonan {

  /**
   * @return BTCCentral ticker
   * @throws java.io.IOException
   */
  @GET
  @Path("ticker")
  public BitKonanTicker getBitKonanTickerBTC() throws IOException;

  @GET
  @Path("{currency}_ticker")
  BitKonanTicker getBitKonanTicker(@PathParam("currency") String currency) throws IOException;

  @GET
  @Path("{currency}_orderbook")
  BitKonanOrderBook getBitKonanOrderBook(@PathParam("currency") String currency) throws IOException;

}

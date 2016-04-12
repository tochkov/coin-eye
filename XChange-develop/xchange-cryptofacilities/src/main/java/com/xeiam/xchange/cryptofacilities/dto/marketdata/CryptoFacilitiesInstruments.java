package com.xeiam.xchange.cryptofacilities.dto.marketdata;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xeiam.xchange.cryptofacilities.dto.CryptoFacilitiesResult;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Neil Panchen
 */

public class CryptoFacilitiesInstruments extends CryptoFacilitiesResult {
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");

    private final Date serverTime;
    private final List<CryptoFacilitiesInstrument> instruments;
	
  
    public CryptoFacilitiesInstruments(@JsonProperty("result") String result
		  , @JsonProperty("serverTime") String strServerTime
		  , @JsonProperty("error") String error
		  , @JsonProperty("instruments") List<CryptoFacilitiesInstrument> instruments) throws ParseException {

	super(result, error);

	this.serverTime = DATE_FORMAT.parse(strServerTime);
	this.instruments = instruments;
    }

    public List<CryptoFacilitiesInstrument> getInstruments() {
            return instruments;
    }
  
    @Override
    public String toString() {

        if(isSuccess()) {
            String res = "CryptoFacilitiesInstruments [serverTime=" + DATE_FORMAT.format(serverTime) + ",instruments=";
            for(CryptoFacilitiesInstrument ct : instruments)
                    res = res + ct.toString() + ", ";
            res = res + " ]";

            return res;
        } 
        else {
            return super.toString();
        }
    }

}

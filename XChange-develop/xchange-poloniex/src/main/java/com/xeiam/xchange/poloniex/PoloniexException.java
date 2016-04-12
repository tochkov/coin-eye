package com.xeiam.xchange.poloniex;

import si.mazi.rescu.HttpStatusExceptionSupport;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("serial")
public class PoloniexException extends HttpStatusExceptionSupport {

  @JsonProperty("error")
  private String error;

  public PoloniexException(@JsonProperty("error") String error) {

    super(error);
    this.error = error;
  }

  public String getError() {

    return error;
  }
}

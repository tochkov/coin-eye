package com.xeiam.xchange.ripple.service.polling;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ripple.dto.RippleException;
import com.xeiam.xchange.ripple.dto.account.ITransferFeeSource;
import com.xeiam.xchange.ripple.dto.account.RippleAccountBalances;
import com.xeiam.xchange.ripple.dto.account.RippleAccountSettings;

public class RippleAccountServiceRaw extends RippleBasePollingService implements ITransferFeeSource {

  private final Map<String, RippleAccountSettings> accountSettingsStore = new ConcurrentHashMap<String, RippleAccountSettings>();

  public RippleAccountServiceRaw(final Exchange exchange) {
    super(exchange);
  }

  public RippleAccountBalances getRippleAccountBalances() throws IOException {
    return getRippleAccountBalances(exchange.getExchangeSpecification().getApiKey());
  }

  public RippleAccountBalances getRippleAccountBalances(final String address) throws IOException {
    return ripplePublic.getAccountBalances(address);
  }

  public RippleAccountSettings getRippleAccountSettings() throws IOException {
    return getRippleAccountSettings(exchange.getExchangeSpecification().getApiKey());
  }

  public RippleAccountSettings getRippleAccountSettings(final String address) throws RippleException, IOException {
    RippleAccountSettings settings = accountSettingsStore.get(address);
    if (settings == null) {
      settings = ripplePublic.getAccountSettings(address);
      accountSettingsStore.put(address, settings);
    }
    return settings;
  }

  @Override
  public BigDecimal getTransferFeeRate(final String address) throws RippleException, IOException {
    final RippleAccountSettings accountSettings = getRippleAccountSettings(address);
    return accountSettings.getSettings().getTransferFeeRate().stripTrailingZeros();
  }

  /**
   * Clear any stored account settings to allow memory to be released and details to be refreshed.
   */
  public void clearAccountSettingsStore() {
    accountSettingsStore.clear();
  }
}

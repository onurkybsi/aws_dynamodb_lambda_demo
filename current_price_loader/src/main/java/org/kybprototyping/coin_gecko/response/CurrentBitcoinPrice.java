package org.kybprototyping.coin_gecko.response;

import java.math.BigDecimal;

public class CurrentBitcoinPrice {
  private BigDecimal eur;

  public BigDecimal getEur() {
    return eur;
  }

  public void setEur(BigDecimal eur) {
    this.eur = eur;
  }
}

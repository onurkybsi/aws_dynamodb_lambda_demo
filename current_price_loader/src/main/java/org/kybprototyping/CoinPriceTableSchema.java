package org.kybprototyping;

import java.math.BigDecimal;

public class CoinPriceTableSchema {
  private String id;
  private BigDecimal price;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }
}

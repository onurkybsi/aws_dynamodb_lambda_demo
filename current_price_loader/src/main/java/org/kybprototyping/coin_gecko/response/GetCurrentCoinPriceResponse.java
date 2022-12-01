package org.kybprototyping.coin_gecko.response;

public class GetCurrentCoinPriceResponse {
  private CurrentBitcoinPrice bitcoin;

  public CurrentBitcoinPrice getBitcoin() {
    return bitcoin;
  }

  public void setBitcoin(CurrentBitcoinPrice bitcoin) {
    this.bitcoin = bitcoin;
  }
}

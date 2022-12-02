package org.kybprototyping.coin_gecko.response;

import java.math.BigDecimal;

/**
 * Represents the response object of the
 * <a href="https://www.coingecko.com/en/api/documentation">CoinGecko</a>
 * endpoint <i>/simple/price</i>
 */
public class GetCurrentCoinPriceResponse {
	private CurrentBitcoinPrice bitcoin;

	public CurrentBitcoinPrice getBitcoin() {
		return bitcoin;
	}

	public void setBitcoin(CurrentBitcoinPrice bitcoin) {
		this.bitcoin = bitcoin;
	}

	public static GetCurrentCoinPriceResponse of(BigDecimal currentBitcoinPrice) {
		GetCurrentCoinPriceResponse response = new GetCurrentCoinPriceResponse();
		response.setBitcoin(new CurrentBitcoinPrice());
		response.getBitcoin().setEur(currentBitcoinPrice);
		return response;
	}
}

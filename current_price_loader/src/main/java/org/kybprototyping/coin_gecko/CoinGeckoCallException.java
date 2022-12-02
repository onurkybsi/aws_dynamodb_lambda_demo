package org.kybprototyping.coin_gecko;

/**
 * Represents the exceptions which are occurred during
 * <a href="https://www.coingecko.com/en/api/documentation">CoinGecko</a> REST
 * API call
 */
public class CoinGeckoCallException extends RuntimeException {
	public CoinGeckoCallException(String message) {
		super(message);
	}

	public CoinGeckoCallException(String message, Throwable cause) {
		super(message, cause);
	}
}

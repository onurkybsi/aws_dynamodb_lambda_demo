package org.kybprototyping.coin_gecko;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.kybprototyping.coin_gecko.response.CurrentBitcoinPrice;
import org.kybprototyping.coin_gecko.response.GetCurrentCoinPriceResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

/**
 * Client for <a href="https://www.coingecko.com/en/api/documentation">CoinGecko
 * REST API</a>
 */
public final class CoinGeckoClient {
	private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().build();
	private static final ObjectMapper OBJECT_MAPPER_INSTANCE = new ObjectMapper();

	private CoinGeckoClient() {
	}

	public static CurrentBitcoinPrice getBitcoinCurrentEuroPrice() {
		try {
			HttpRequest request = java.net.http.HttpRequest.newBuilder()
					.GET()
					.uri(URI.create("https://api.coingecko.com/api/v3/simple/price?ids=bitcoin&vs_currencies=eur"))
					.build();
			HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
			return OBJECT_MAPPER_INSTANCE.readValue(response.body(), GetCurrentCoinPriceResponse.class).getBitcoin();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new CoinGeckoCallException(e.getMessage(), e);
		} catch (IOException e) {
			throw new CoinGeckoCallException(e.getMessage(), e);
		}
	}
}

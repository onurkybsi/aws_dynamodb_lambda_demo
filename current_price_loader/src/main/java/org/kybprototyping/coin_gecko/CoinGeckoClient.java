package org.kybprototyping.coin_gecko;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kybprototyping.coin_gecko.response.CurrentBitcoinPrice;
import org.kybprototyping.coin_gecko.response.GetCurrentCoinPriceResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public final class CoinGeckoClient {
  private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().build();
  private static final ObjectMapper OBJECT_MAPPER_INSTANCE = new ObjectMapper();

  private CoinGeckoClient() {
  }

  public static CurrentBitcoinPrice getBitcoinCurrentEuroPrice(LambdaLogger logger) {
    try {
      HttpRequest request = java.net.http.HttpRequest.newBuilder()
          .GET()
          .uri(URI.create("https://api.coingecko.com/api/v3/simple/price?ids=bitcoin&vs_currencies=eur"))
          .build();
      HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
      logger.log(String.format("STATUS_CODE: %d && RESPONSE_BODY: %s", response.statusCode(), response.body()));
      return OBJECT_MAPPER_INSTANCE.readValue(response.body(), GetCurrentCoinPriceResponse.class).getBitcoin();
    } catch (IOException | InterruptedException e) {
      Thread.currentThread().interrupt();
      return null;
    }
  }
}

package org.kybprototyping;

import com.amazonaws.services.lambda.runtime.Context;
import org.kybprototyping.coin_gecko.CoinGeckoClient;
import org.kybprototyping.coin_gecko.response.CurrentBitcoinPrice;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

import java.util.Map;

public class CurrentPriceLoader {
  private static final DynamoDbClient DYNAMO_DB_CLIENT = DynamoDbClient.builder()
      .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
      .region(Region.EU_CENTRAL_1)
      .build();

  public void load(Context context) {
    context.getLogger().log("Started...");

    var currentBitcoinPrice = CoinGeckoClient.getBitcoinCurrentEuroPrice(context.getLogger());
    context.getLogger()
        .log(String.format("Current Bitcoint EUR price: %.2f has been fetched from GoinGecko%n", currentBitcoinPrice.getEur()));
    context.getLogger().log("It's being stored on DynamoDB...%n");

    PutItemResponse creationResult = createItemOnCoinPrice(currentBitcoinPrice);
    context.getLogger().log(String.format("Item creation was completed, status code: %d", creationResult.sdkHttpResponse().statusCode()));
  }

  private static PutItemResponse createItemOnCoinPrice(CurrentBitcoinPrice currentBitcoinPrice) {
    PutItemRequest request = PutItemRequest.builder()
        .tableName("coin_price")
        .item(Map.of("id", AttributeValue.fromS("bitcoin"),
            "price", AttributeValue.fromN(currentBitcoinPrice.getEur().toString())))
        .build();
    return DYNAMO_DB_CLIENT.putItem(request);
  }
}

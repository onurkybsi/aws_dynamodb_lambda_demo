package org.kybprototyping;

import com.amazonaws.services.lambda.runtime.Context;
import org.kybprototyping.coin_gecko.CoinGeckoClient;
import org.kybprototyping.coin_gecko.response.CurrentBitcoinPrice;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

import java.util.Map;

/**
 * Refreshes the current Bitcoin price on the DynamoDB
 */
public class CurrentPriceLoader {

	public void load(Context context) {
		context.getLogger().log("CurrentPriceLoader started...");

		context.getLogger().log("Current price is being fetched from CoinGecko...");
		var currentBitcoinPrice = CoinGeckoClient.getBitcoinCurrentEuroPrice();
		context.getLogger()
				.log(String.format("Current Bitcoin EUR price has been fetched from GoinGecko as %.2f",
						currentBitcoinPrice.getEur()));

		context.getLogger().log("Current price being stored on DynamoDB...");
		PutItemResponse creationResult = storeCurrentPriceOnDynamoDb(currentBitcoinPrice);
		context.getLogger().log(
				String.format("Storing completed, SDK status code: %d", creationResult.sdkHttpResponse().statusCode()));
	}

	private static PutItemResponse storeCurrentPriceOnDynamoDb(CurrentBitcoinPrice currentBitcoinPrice) {
		PutItemRequest request = PutItemRequest.builder()
				.tableName("coin_price")
				.item(Map.of("id", AttributeValue.fromS("bitcoin"),
						"price", AttributeValue.fromN(currentBitcoinPrice.getEur().toString())))
				.build();
		return LambdaHandlerHelper.getDynamoDbClient().putItem(request);
	}
}

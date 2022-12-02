package org.kybprototyping;

import org.kybprototyping.coin_gecko.response.GetCurrentCoinPriceResponse;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import java.math.BigDecimal;
import java.util.Map;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;

/**
 * Receives current Bitcoin price by querying DynamoDB
 */
public class CurrentPriceReceiver {
	private static final Map<String, String> QUERY_EXPRESSION_ATTRIBUTE_NAMES = Map.of("#id", "id");
	private static final Map<String, AttributeValue> QUERY_EXPRESSION_ATTRIBUTE_VALUES = Map.of(":idValue",
			AttributeValue.builder().s("bitcoin").build());

	public GetCurrentCoinPriceResponse get(Context context) {
		context.getLogger().log("CurrentPriceReceiver started...");
		return getCurrentPriceFromDynamoDb(context.getLogger());
	}

	private static GetCurrentCoinPriceResponse getCurrentPriceFromDynamoDb(LambdaLogger logger) {
		logger.log("Query is sending to DynamoDB...");
		QueryResponse response = LambdaHandlerHelper.getDynamoDbClient().query(buildQueryRequest());
		logger.log(String.format("Query result: %s", response));

		logger.log("CurrentPriceReceiver completed!");
		return GetCurrentCoinPriceResponse.of(new BigDecimal(response.items().get(0).get("price").n()));
	}

	private static QueryRequest buildQueryRequest() {
		return QueryRequest.builder()
				.tableName("coin_price")
				.keyConditionExpression("#id = :idValue")
				.expressionAttributeNames(QUERY_EXPRESSION_ATTRIBUTE_NAMES)
				.expressionAttributeValues(QUERY_EXPRESSION_ATTRIBUTE_VALUES)
				.build();
	}

}

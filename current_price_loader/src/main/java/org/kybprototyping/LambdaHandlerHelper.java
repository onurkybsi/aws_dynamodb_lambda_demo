package org.kybprototyping;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

/**
 * Contains common operations for the Lambda handlers
 */
final class LambdaHandlerHelper {
	private static final DynamoDbClient DYNAMO_DB_CLIENT = DynamoDbClient.builder()
			.credentialsProvider(EnvironmentVariableCredentialsProvider.create())
			.region(Region.EU_CENTRAL_1)
			.build();

	private LambdaHandlerHelper() {
	}

	public static DynamoDbClient getDynamoDbClient() {
		return DYNAMO_DB_CLIENT;
	}

}

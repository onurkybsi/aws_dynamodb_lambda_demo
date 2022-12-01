import { Stack, StackProps } from "aws-cdk-lib";
import { Construct } from "constructs";
import * as dynamodb from "aws-cdk-lib/aws-dynamodb";
import { Table } from "aws-cdk-lib/aws-dynamodb";

export class DynamoDBStack extends Stack {
	private coinPriceTable: Table;

	constructor(scope: Construct, id: string, props?: StackProps) {
		super(scope, id, props);

		const coinPriceTable = new dynamodb.Table(this, "CoinPriceTable", {
			tableName: "coin_price",
			partitionKey: { name: "id", type: dynamodb.AttributeType.STRING },
			billingMode: dynamodb.BillingMode.PROVISIONED,
			// One read capacity unit represents one strongly consistent read per second, or two eventually consistent reads per second, for an item up to 4 KB in size.
			readCapacity: 3,
			// One write capacity unit represents one write per second for an item up to 1 KB in size
			writeCapacity: 3,
		});
		const readScaling = coinPriceTable.autoScaleReadCapacity({ minCapacity: 5, maxCapacity: 10 });
		readScaling.scaleOnUtilization({ targetUtilizationPercent: 90 })

		const writeScaling = coinPriceTable.autoScaleWriteCapacity({ minCapacity: 5, maxCapacity: 10 });
		writeScaling.scaleOnUtilization({ targetUtilizationPercent: 90 })

		this.coinPriceTable = coinPriceTable;
	}

	public getCoinPriceTable(): Table {
		return this.coinPriceTable;
	}
}

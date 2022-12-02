import { Stack, StackProps } from "aws-cdk-lib";
import { Construct } from "constructs";
import { Function, Runtime, Code, Architecture, Handler } from 'aws-cdk-lib/aws-lambda';
import * as cdk from 'aws-cdk-lib';
import { IRepository } from "aws-cdk-lib/aws-ecr";
import { Table } from "aws-cdk-lib/aws-dynamodb";

interface LambdaStackProps extends StackProps {
	lambdaFunctionNameRepositoryPairs: [string, IRepository][];
	coinPriceDynamoDbTable: Table;
}

export class LambdaStack extends Stack {

	constructor(scope: Construct, id: string, props: LambdaStackProps) {
		super(scope, id, props);

		props.lambdaFunctionNameRepositoryPairs
			.forEach(pair => this.buildLambdaFunction(pair[0], pair[1], props.coinPriceDynamoDbTable));
	}

	private buildLambdaFunction(name: string, repository: IRepository, coinPriceDynamoDbTable: Table): void {
		const lambda = new Function(this, `${name}Lambda`, {
			functionName: name,
			runtime: Runtime.FROM_IMAGE,
			memorySize: 1024,
			timeout: cdk.Duration.seconds(10),
			handler: Handler.FROM_IMAGE,
			code: Code.fromEcrImage(repository),
			architecture: Architecture.ARM_64, /* My CPU has arm64 arch */
			environment: {
				REGION: cdk.Stack.of(this).region,
				AVAILABILITY_ZONES: JSON.stringify(
					cdk.Stack.of(this).availabilityZones,
				),
			},
		});
		repository.grantPull(lambda);
		coinPriceDynamoDbTable.grantFullAccess(lambda);
	}
}
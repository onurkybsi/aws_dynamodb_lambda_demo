import { Stack, StackProps } from "aws-cdk-lib";
import { Construct } from "constructs";
import * as lambda from 'aws-cdk-lib/aws-lambda';
import * as cdk from 'aws-cdk-lib';
import { IRepository } from "aws-cdk-lib/aws-ecr";
import { Architecture, Handler } from "aws-cdk-lib/aws-lambda";
import { Table } from "aws-cdk-lib/aws-dynamodb";

interface CurrentPriceLoaderLambdaStackProps extends StackProps {
	repositoryOfLambdaFunctionHandlers: IRepository;
	coinPriceTable: Table;
}

export class CurrentPriceLoaderLambdaStack extends Stack {
	constructor(scope: Construct, id: string, props: CurrentPriceLoaderLambdaStackProps) {
		super(scope, id, props);

		// 👇 lambda function definition
		let currentPriceLoaderLambda = new lambda.Function(this, "CurrentPriceLoaderLambda", {
			functionName: "CurrentPriceLoader",
			runtime: lambda.Runtime.FROM_IMAGE,
			memorySize: 1024,
			timeout: cdk.Duration.seconds(5),
			handler: Handler.FROM_IMAGE,
			code: lambda.Code.fromEcrImage(props.repositoryOfLambdaFunctionHandlers),
			architecture: Architecture.ARM_64, /* My CPU has arm64 arch */
			environment: {
				REGION: cdk.Stack.of(this).region,
				AVAILABILITY_ZONES: JSON.stringify(
					cdk.Stack.of(this).availabilityZones,
				),
			},
		});
		props.repositoryOfLambdaFunctionHandlers.grantPull(currentPriceLoaderLambda);
		props.coinPriceTable.grantFullAccess(currentPriceLoaderLambda);
	}
}

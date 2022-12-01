#!/usr/bin/env node
import "source-map-support/register";
import * as cdk from "aws-cdk-lib";
import { DynamoDBStack } from "../lib/dynamodb-stack";
import { CurrentPriceLoaderLambdaStack } from "../lib/current-price-loader-lambda-stack";
import { LambdaEcrRepositoryStack } from "../lib/lambda-ecr-repository-stack";

const app = new cdk.App();
// stacks
const dynamodb = new DynamoDBStack(app, "DynamoDBStack");
const lambdaEcrRepositoryStack = new LambdaEcrRepositoryStack(app, "LambdaEcrRepositoryStack");
new CurrentPriceLoaderLambdaStack(app, "CurrentPriceLoaderLambdaStack", {
	repositoryOfLambdaFunctionHandlers: lambdaEcrRepositoryStack.getCreatedLambdaEcrRepository(),
	coinPriceTable: dynamodb.getCoinPriceTable()
});
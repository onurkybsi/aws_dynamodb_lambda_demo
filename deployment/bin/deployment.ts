#!/usr/bin/env node
import "source-map-support/register";
import * as cdk from "aws-cdk-lib";
import { DynamoDBStack } from "../lib/dynamodb-stack";
import { LambdaStack } from "../lib/lambda-stack";
import { EcrStack } from "../lib/ecr-stack";

const app = new cdk.App();
// stacks
const dynamodb = new DynamoDBStack(app, "DynamoDBStack");
const ecrStack = new EcrStack(app, "EcrStack", {
	repositoryNames: ["current-price-loader", "current-price-receiver"]
});
new LambdaStack(app, "LambdaStack", {
	lambdaFunctionNameRepositoryPairs: [
		["current-price-loader", ecrStack.getRepositoryByName("current-price-loader")],
		["current-price-receiver", ecrStack.getRepositoryByName("current-price-receiver")]],
	coinPriceDynamoDbTable: dynamodb.getCoinPriceTable()
});
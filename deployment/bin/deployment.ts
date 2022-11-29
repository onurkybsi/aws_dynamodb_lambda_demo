#!/usr/bin/env node
import 'source-map-support/register';
import * as cdk from 'aws-cdk-lib';
import { DynamoDBStack } from '../lib/dynamodb-stack';

const app = new cdk.App();
new DynamoDBStack(app, 'DynamoDBStack', {
});